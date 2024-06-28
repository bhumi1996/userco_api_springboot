package com.secui.entity;

import com.secui.utility.ConstantUtil;
import com.secui.utility.TableUtility;
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
@Table(name = TableUtility.CAMPAIGN_TYPE_MSTR_TBL)
public class CampaignTypeEntity extends Auditable{

    @Column(name = ConstantUtil.TYPE)
    private String type;

}
