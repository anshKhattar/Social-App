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
public class CommentModel {
    @Id
    @MongoId
    private String id;
    private String postId;
    private String userId;
    private String parentId;
    private String description;
}
