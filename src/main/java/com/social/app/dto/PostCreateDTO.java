package com.social.app.dto;

import com.social.app.enums.ContentTypeEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
@Builder
public class PostCreateDTO{
    private String id;
    private String description;
    private MultipartFile content;
    private ContentTypeEnum contentType;
}
