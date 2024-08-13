package com.secui.mvc.entity;

import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.SERVICE_MSTR_TBL)
public class ServiceEntity extends Auditable{

    @Column(name = ColumnUtils.SERVICE_NAME)
    private String serviceName;
    @Column(name = ColumnUtils.DESCRIPTION)
    private String description;
    @Column(name = ColumnUtils.IMAGE_URL)
    private String imageUrl;

}
