package com.social.app.dto.request;

import com.social.app.enums.RoleTypeEnum;
import lombok.Data;

import java.util.List;

@Data

public class SignupRequest {
    private String name;
    private String username;
    private String email;
    private int age;
    private String gender;
    private String password;
    private String profilePic;
}
