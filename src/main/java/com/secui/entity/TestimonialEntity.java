package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = TableUtility.TESTIMONIAL_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialEntity extends Auditable{

    @Column(name = ColumnUtils.NAME)
    private String name;

    @Column(name = ColumnUtils.LOCATION)
    private String location;

    @Column(name = ColumnUtils.DESIGNATION)
    private String designation;

    @Column(name = ColumnUtils.COMMENTS,columnDefinition = ColumnUtils.TEXT)
    private String comments;

    @Column(name=ColumnUtils.FILE_NAME,columnDefinition=ColumnUtils.TEXT)
    private String fileName;

    @Transient
    private MultipartFile profileImage;
}
