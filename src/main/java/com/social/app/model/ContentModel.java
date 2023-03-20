package com.social.app.model;

import com.social.app.enums.ContentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "content")
public class ContentModel {
    @Id
    @MongoId
    private String id;
    private ContentTypeEnum type;
    private String link;
}
