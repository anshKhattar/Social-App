package com.social.app.dto;

import com.social.app.enums.ContentTypeEnum;
import com.social.app.model.UserDetails;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class PostResponseDTO{
    private String id;
    private String description;
    private String contentLink;
    private ContentTypeEnum contentType;
    private UserDetails user;
    private VoteCountDTO votes;
    private boolean isPublished;
}
