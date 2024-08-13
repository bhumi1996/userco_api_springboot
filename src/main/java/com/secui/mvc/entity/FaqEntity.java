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
@Table(name = TableUtility.FAQ_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FaqEntity extends Auditable{
    @Column(name = ColumnUtils.FAQ_QUESTION)
    private String faqQuestion;
    @Column(name = ColumnUtils.FAQ_ANSWER)
    private String faqAnswer;
}
