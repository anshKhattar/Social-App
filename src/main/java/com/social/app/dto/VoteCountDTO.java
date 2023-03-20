package com.social.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteCountDTO {
    private Number likeCount;
    private Number dislikeCount;
}
