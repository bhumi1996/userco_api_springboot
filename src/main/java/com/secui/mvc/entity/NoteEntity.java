package com.secui.mvc.entity;

import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.TableUtility;
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
@Table(name = TableUtility.NOTE_MSTR_TBL)
public class NoteEntity extends Auditable{

    @Column(name = ColumnUtils.HEADING)
    private String heading;
    @Column(name = ColumnUtils.MESSAGE)
    private String message;

    @ManyToOne
    @JoinColumn(name= ColumnUtils.LEAD_ID,nullable = false)
    private LeadEntity leadEntity;
}
