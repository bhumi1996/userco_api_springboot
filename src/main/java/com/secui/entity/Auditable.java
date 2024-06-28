package com.secui.entity;


import com.secui.utility.ColumnUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @Id
    @Column(name = ColumnUtils.PID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    @Column(name = ColumnUtils.UKEY,updatable = false,unique = true)
    private String uKey;

    @CreatedBy
    @Column(name = ColumnUtils.CREATED_BY)
    private String createdBy;

    @CreatedDate
    @Column(name = ColumnUtils.CREATED_DT)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = ColumnUtils.LST_MODY_BY)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = ColumnUtils.LST_MODY_DT)
    private Date lastModifiedDate;

    @Column(name = ColumnUtils.STATUS)
    private String status;

}

