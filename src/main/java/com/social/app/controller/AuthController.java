package com.social.app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.social.app.enums.RoleTypeEnum;
import com.social.app.model.Role;
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
import com.social.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        RoleTypeEnum role = userDetails.getRole();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
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

        // Create new user's account
//        User user = new User(
//                signUpRequest.getName(),
//                signUpRequest.getUsername(),
//                signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()),
//                signUpRequest.getRoles());
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

        return ResponseEntity.ok(new MessageResponse("user registered successfully!"));
    }
}
