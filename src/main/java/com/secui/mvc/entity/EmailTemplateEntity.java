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
@Table(name = TableUtility.EMAIL_TEMPLATE_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateEntity extends Auditable{
    @Column
    private String templateName;

    @Column
    private String templateHeading;

    @Column(name= ColumnUtils.TEMPLATE_BODY, columnDefinition= ConstantUtil.LONG_TEXT)
    private String templateBody;
}
