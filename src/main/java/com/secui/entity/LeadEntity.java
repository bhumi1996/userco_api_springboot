package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.ConstantUtil;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.LEAD_MSTR_TBL)
public class LeadEntity extends Auditable{

    @Column(name= ColumnUtils.LEAD_NAME)
    private String leadName;
    @Column(name= ColumnUtils.EMAIL)
    private String email;
    @Column(name= ColumnUtils.DAIL_CODE)
    private String dialCode;
    @Column(name= ColumnUtils.MOBILE_NUMBER)
    private String mobileNumber;
    @Column(name= ColumnUtils.COUNTRY)
    private String country;
    @Column(name= ColumnUtils.STAGE)
    private String stage;
    @Column(name= ColumnUtils.CHANNEL)
    private String channel;
    @Column(name= ColumnUtils.GENDER)
    private String gender;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = ColumnUtils.LEAD_ENTITY, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<MailEntity> mailEntity = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = ColumnUtils.LEAD_ENTITY, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<NoteEntity> noteEntity = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = ColumnUtils.LEAD_GROUPS, joinColumns = @JoinColumn(name = ColumnUtils.LEAD_ID, referencedColumnName = ConstantUtil.PID), inverseJoinColumns = @JoinColumn(name = ColumnUtils.GROUP_PID, referencedColumnName = ConstantUtil.PID))
    private Set<GroupEntity> groups = new LinkedHashSet<>();

}
