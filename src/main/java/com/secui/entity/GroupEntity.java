package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = TableUtility.GROUP_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity extends Auditable{

    @Column(name = ColumnUtils.GROUP_NAME)
    private String groupName;
    @Column(name = ColumnUtils.DESCRIPTION,columnDefinition = ColumnUtils.LONG_TEXT)
    private String description;

    @ManyToMany(mappedBy = ColumnUtils.GROUPS,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<LeadEntity> leads =new LinkedHashSet<>();

    @ManyToMany(mappedBy = ColumnUtils.GROUPS,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<CampaignEntity>campaigns =new LinkedHashSet<>();
}
