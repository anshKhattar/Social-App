package com.social.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String id;
    private String name;
    private int age;
    private String gender;
    private MultipartFile profilePic;
}

