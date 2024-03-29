package com.social.app.service;

import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;


@Service
public class UserPasswordService {
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${site_url}")
    private String siteURL;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder encoder;


    private String forgetPasswordURL = "/api/auth/resetForgetPassword?token=";
    private String resetPasswordURL = "api/auth/resetPassword?token=";
    public String updateForgetPassword(String userId, String forgetPassword){
            User dbUser = userDetailsService.getUserByUserId(userId);
            dbUser.setPassword(encoder.encode(forgetPassword));
            userRepository.save(dbUser);
            return "Password reset successfully.";
    }
    public void forgetPassword(String email) throws MessagingException, UnsupportedEncodingException {
        sendMail(email,forgetPasswordURL);
    }

    public String resetPassword(Map<String,String> body, User user){
        String msg;
        System.out.println(user);
        if(encoder.matches(body.get("oldPassword"), user.getPassword())){
            user.setPassword(encoder.encode(body.get("newPassword")));
            userRepository.save(user);
            msg = "Password reset successfully.";
        }
        else {
            msg = "The old password that you entered is invalid.";
        }
        return msg;
    }

    public void sendMail(String email, String mainURL) throws MessagingException, UnsupportedEncodingException {
        User user = userDetailsService.loadUserByUserEmail(email);

        String toAddress = email;
        String fromAddress = senderEmailAddress;
        String senderName = "Tekion Social App";
        String subject = "Please reset you password your registration";
        String content = "Dear " + user.getName() + ",<br>"
                         +"Please click the link below to reset your password:<br>"
                         + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET</a></h3>"
                         + "Thank you,<br>"
                         + "Tekion Social App";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL =
                siteURL + mainURL + jwtUtils.generateForgetPasswordToken(user.getId());

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
