package com.social.app.service;

import com.social.app.model.User;
import com.social.app.model.UserDetails;
import com.social.app.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;


import java.io.UnsupportedEncodingException;

@Service
@Data
public class UserVerificationService {

    @Value("${spring.mail.username}")
    private String senderEmailAddress;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${site_url}")
    private String siteURL;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private  UserDetailsServiceImpl userDetailsServiceImpl;

    public void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = senderEmailAddress;
        String senderName = "Tekion Social App";
        String subject = "Please verify your registration";
        String content = "Dear " + user.getName()+",<br>"
                         + "Please click the link below to verify your registration:<br>"
                         + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                         + "Thank you,<br>"
                         + "Tekion Social App";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = siteURL + "/api/auth/verify?code=" + getVerificationCode(user.getId());

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public String getVerificationCode(String userId){
        return jwtUtils.generateVerifyToken(userId);
    }


    public void enableUser(String userId){
        userDetailsServiceImpl.saveEnableUser(userId);
    }

}
