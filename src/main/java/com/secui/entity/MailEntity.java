package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.MAIL_ENTITY_MSTR_TBL)
public class MailEntity extends Auditable{

    @Column(name = ColumnUtils.EMAIL)
    String email;
    @Column(name = ColumnUtils.TEMPLATE_NAME)
    String templateName;
    @Column(name = ColumnUtils.SUBJECT)
    String subject;
    @Column(name = ColumnUtils.MESSAGE,columnDefinition = ColumnUtils.LONG_TEXT)
    String message;

    @ManyToOne
    @JoinColumn(name= ColumnUtils.LEAD_ID,nullable = false)
    private LeadEntity leadEntity;
}
