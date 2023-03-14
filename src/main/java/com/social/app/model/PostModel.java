package com.social.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModel {
    @Id
    @MongoId
    private String id;
    private String title;
    private String description;
    private String content_id;
    private boolean is_published;
    private String user_id;

}
