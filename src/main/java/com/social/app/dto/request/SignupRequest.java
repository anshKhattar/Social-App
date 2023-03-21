package com.social.app.dto.request;

import lombok.Data;
@Data

public class SignupRequest {
    private String name;
    private String username;
    private String email;
    private int age;
    private String gender;
    private String password;
    // private String profilePic;
}
