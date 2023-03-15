package com.social.app.model;

import com.social.app.enums.VoteTypeEnum;
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
public class VoteModel {
    @Id
    @MongoId
    private String id;
    private VoteTypeEnum voteType;
    private String postId;
    private String userId;
}
