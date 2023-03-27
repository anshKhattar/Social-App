package com.social.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDTO {

    private String id;
    private String name;
    private int age;
    private String gender;
    private String profilePic;
}
