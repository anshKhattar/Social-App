package com.social.app.service;

import com.social.app.dto.request.LoginRequest;
import com.social.app.dto.request.SignupRequest;
import com.social.app.dto.response.JwtResponse;
import com.social.app.dto.response.MessageResponse;
import com.social.app.enums.RoleTypeEnum;
import com.social.app.helpers.CloudinaryService;
import com.social.app.model.User;
import com.social.app.model.UserDetails;
import com.social.app.repository.UserDetailsRepository;
import com.social.app.repository.UserRepository;
import com.social.app.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.UnsupportedEncodingException;

@Service
public class UserAuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserPasswordService userPasswordService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    UserVerificationService userVerificationService;
    public ResponseEntity<?> userLogin(LoginRequest loginRequest){
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

    public ResponseEntity<?> userSignup(SignupRequest signUpRequest) throws MessagingException,
            UnsupportedEncodingException {
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

        String profilePic = cloudinaryService.upload(signUpRequest.getProfilePic());

        UserDetails userDetails = new UserDetails(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getAge(),
                signUpRequest.getGender(),
                profilePic,
                dbUser.getId());
        userDetailsRepository.save(userDetails);

        // sending verification mail to the user

        userVerificationService.sendVerificationEmail(dbUser);


        return ResponseEntity.ok(new MessageResponse("user registered successfully! email for verification sent " +
                                                     "to the mentioned email please validate within 15 minutes."));
    }



    public String forgetPassword(String email) throws MessagingException,
            UnsupportedEncodingException {
        String msg;

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
}
