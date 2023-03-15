package com.social.app.dto.request;

import com.social.app.model.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data

public class SignupRequest {
    private String name;
    private String username;
    private String email;
    private int age;
    private String gender;
    private List<ERole> roles;
    private String password;
    private String profilePic;
}
