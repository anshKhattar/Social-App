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

/**
 * The type Auth controller.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    /**
     * The User verification service.
     */
    @Autowired
    UserVerificationService userVerificationService;
    /**
     * The User auth service.
     */
    @Autowired
    UserAuthService userAuthService;
    /**
     * The Jwt utils.
     */
    @Autowired
    JwtUtils jwtUtils;

    @Value("${JWT_VERIFY_SECRET}")
    private String jwtVerifySecret;
    @Value("${JWT_FORGET_SECRET}")
    private String jwtForgetSecret;
    @Autowired
    private UserPasswordService userPasswordService;


    /**
     * Authenticate user response entity.
     *
     * @param loginRequest the login request
     * @return the response entity
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userAuthService.userLogin(loginRequest);
    }

    /**
     * Register user response entity.
     *
     * @param signUpRequest the sign up request
     * @return the response entity
     * @throws MessagingException           the messaging exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@ModelAttribute SignupRequest signUpRequest)
            throws MessagingException, UnsupportedEncodingException {
        return userAuthService.userSignup(signUpRequest);
    }


    /**
     * Verify user string.
     *
     * @param token the token
     * @return the string
     */
    @GetMapping("/verify")
    public String verifyUser(@RequestParam(value="code") String token){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtVerifySecret);
        return userVerificationService.enableUser(userId);
    }


    /**
     * Forget password string.
     *
     * @param body the body
     * @return the string
     * @throws MessagingException           the messaging exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @PostMapping("/forgetPassword")
    public String forgetPassword(@RequestBody Map<String, String> body)
            throws MessagingException, UnsupportedEncodingException {
        return userAuthService.forgetPassword(body.get("email"));
    }

    /**
     * Reset forget password string.
     *
     * @param token the token
     * @param body  the body
     * @return the string
     */
    @PostMapping ("/resetForgetPassword")
    public String resetForgetPassword(@RequestParam(value="token") String token, @RequestBody Map<String, String> body){
        String userId = jwtUtils.getUserNameFromJwtToken(token,jwtForgetSecret);
        return userPasswordService.updateForgetPassword(userId, body.get("newPassword"));
    }


    /**
     * Reset password string.
     *
     * @param body           the body
     * @param authentication the authentication
     * @return the string
     */
    @PostMapping("/resetPassword")
    public String resetPassword( @RequestBody Map<String, String> body, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return userPasswordService.resetPassword(body,user);

    }

}
