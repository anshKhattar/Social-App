package com.social.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_details")
@AllArgsConstructor
@Builder
public class UserDetails {
    @Id
    private String id;
    private String name;
    private int age;
    private String gender;
    private boolean isEnabled;
    private String profilePic;
    private String userId;

    public UserDetails() {
    }

    public UserDetails(
            String name,
            int age,
            String gender,
            String profilePic,
            String userId) {

        this.name= name;
        this.age = age;
        this.gender = gender;
        this.profilePic = profilePic;
        this.userId = userId;
    }
}
