package com.social.app.dto;

import com.social.app.enums.ContentTypeEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PostResponseDTO{
    private String id;
    private String title;
    private String description;
    private String contentLink;
    private ContentTypeEnum contentType;
    private String userId;
    private VoteCountDTO votes;
}
