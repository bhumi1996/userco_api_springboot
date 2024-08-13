package com.secui.mvc.entity;

import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = TableUtility.ARTICLE_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEntity extends Auditable{

    @Column(name = ColumnUtils.PORTAL_NAME)
    private String portalName;
    @Column(name = ColumnUtils.HEADING)
    private String heading;
    @Column(name = ColumnUtils.TITLE)
    private String title;
    @Column(name = ColumnUtils.URL)
    private String url;
    @Column(name = ColumnUtils.DESCRIPTION,columnDefinition = ConstantUtil.LONG_TEXT)
    private String description;
    @Column(name = ColumnUtils.SHORT_DESCRIPTION)
    private String shortDescription;
    @Column(name = ColumnUtils.IMAGE_URL)
    private String imageUrl;
}
