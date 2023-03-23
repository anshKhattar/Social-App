package com.social.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;


import com.social.app.enums.RoleTypeEnum;
import com.social.app.model.User;
import com.social.app.model.UserDetails;
import com.social.app.dto.request.LoginRequest;
import com.social.app.dto.request.SignupRequest;
import com.social.app.dto.response.JwtResponse;
import com.social.app.dto.response.MessageResponse;
import com.social.app.repository.RoleRepository;
import com.social.app.repository.UserDetailsRepository;
import com.social.app.repository.UserRepository;
import com.social.app.security.jwt.JwtUtils;
import com.social.app.service.UserPasswordService;
import com.social.app.service.UserVerificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserVerificationService userVerificationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${JWT_VERIFY_SECRET}")
    private String jwtVerifySecret;
    @Value("${JWT_FORGET_SECRET}")
    private String jwtForgetSecret;
    @Autowired
    private UserPasswordService userPasswordService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateLoginToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        RoleTypeEnum role = userDetails.getRole();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest)
            throws MessagingException, UnsupportedEncodingException {
        // System.out.println(signUpRequest);
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        User user = User.builder()
                .username(signUpRequest.getUsername())
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(RoleTypeEnum.ROLE_USER)
                        .build();



//        // System.out.println(strRoles);
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(RoleTypeEnum.ROLE_USER)
//                                          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case ROLE_ADMIN:
//                        Role adminRole = roleRepository.findByName(RoleTypeEnum.ROLE_ADMIN)
//                                                       .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//
//                    default:
//                        Role userRole = roleRepository.findByName(RoleTypeEnum.ROLE_USER)
//                                                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }


        User dbUser = userRepository.save(user);

        UserDetails userDetails = new UserDetails(
                signUpRequest.getName(),
                signUpRequest.getAge(),
                signUpRequest.getGender(),
                // signUpRequest.getProfilePic(),
                dbUser.getId());
        userDetailsRepository.save(userDetails);

        // sending verification mail to the user

        userVerificationService.sendVerificationEmail(dbUser);


        return ResponseEntity.ok(new MessageResponse("user registered successfully! \n email for verification sent " +
                                                     "to the mentioned email please validate within 15 minutes."));
    }


    @GetMapping("/verify")
    public String verifyUser(@RequestParam(value="code") String token){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtVerifySecret);
        userVerificationService.enableUser(userId);
        return "Successfully validated";
    }


    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestBody Map<String, String> body) throws MessagingException,
            UnsupportedEncodingException {
        String msg;
        String email = body.get("email");
        if (userRepository.existsByEmail(email)){
            userPasswordService.forgetPassword(email);
            msg="A link to reset your password has been sent to the email you provided. Kindly reset it within 10 " +
                "minutes.";
        }
        else{
            msg="User with provided email does not exist, please provide a valid email.";
        }
        return msg;
    }

    @PostMapping ("/resetForgetPassword")
    public String resetForgetPassword(@RequestParam(value="token") String token, @RequestBody Map<String, String> body){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtForgetSecret);
        // System.out.println(token+" "+body.get("newPassword"));
        userPasswordService.updateForgetPassword(userId, body.get("newPassword"));
        return "Password reset successfully.";
    }


    @PostMapping("/resetPassword")
    public String resetPassword( @RequestBody Map<String, String> body, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return userPasswordService.resetPassword(body,user);

    }

}
