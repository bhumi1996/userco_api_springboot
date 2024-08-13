package com.secui.mvc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleRequestDto {
    private String portalName;
    private String heading;
    private String title;
    private String url;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private MultipartFile image;
    private String status;
}
