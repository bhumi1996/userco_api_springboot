package com.secui.mvc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleResponseDto {
    private String portalName;
    private String heading;
    private String title;
    private String url;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

}
