package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.ConstantUtil;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = TableUtility.CAMPAIGN_MASTER_TABLE)
@ToString
public class CampaignEntity extends Auditable{
    @Column(name = ColumnUtils.NAME)
    String name;

    @Column(name = ColumnUtils.TYPE)
    String type;

    @ManyToMany(cascade= CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = ColumnUtils.CAMPAIGN_GROUPS,
            joinColumns = @JoinColumn(
                    name = ColumnUtils.CAMPAIGN_PID, referencedColumnName = ConstantUtil.PID),
            inverseJoinColumns = @JoinColumn(
                    name = ColumnUtils.GROUP_PID, referencedColumnName = ConstantUtil.PID))
    private Set<GroupEntity> groups =new LinkedHashSet<>();


}
