package com.social.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import com.social.app.model.User;
import com.social.app.dto.request.LoginRequest;
import com.social.app.dto.request.SignupRequest;
import com.social.app.security.jwt.JwtUtils;
import com.social.app.service.UserAuthService;
import com.social.app.service.UserPasswordService;
import com.social.app.service.UserVerificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    UserVerificationService userVerificationService;
    @Autowired
    UserAuthService userAuthService;
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
        return userAuthService.userLogin(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@ModelAttribute SignupRequest signUpRequest)
            throws MessagingException, UnsupportedEncodingException {
        return userAuthService.userSignup(signUpRequest);
    }


    @GetMapping("/verify")
    public String verifyUser(@RequestParam(value="code") String token){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtVerifySecret);
        return userVerificationService.enableUser(userId);
    }


    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestBody Map<String, String> body)
            throws MessagingException, UnsupportedEncodingException {
        return userAuthService.forgetPassword(body.get("email"));
    }

    @PostMapping ("/resetForgetPassword")
    public String resetForgetPassword(@RequestParam(value="token") String token, @RequestBody Map<String, String> body){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtForgetSecret);
        return userPasswordService.updateForgetPassword(userId, body.get("newPassword"));
    }


    @PostMapping("/resetPassword")
    public String resetPassword( @RequestBody Map<String, String> body, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return userPasswordService.resetPassword(body,user);

    }

}
