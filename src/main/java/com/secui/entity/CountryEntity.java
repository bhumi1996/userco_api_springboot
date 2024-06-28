package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.COUNTRY_MSTR_TBL)
public class CountryEntity extends Auditable{

    @Column(name = ColumnUtils.NAME)
    private String name;
    @Column(name = ColumnUtils.CAPITAL)
    private String capital;
    @Column(name = ColumnUtils.ISOTWO)
    private String isoTwo;
    @Column(name = ColumnUtils.ISOTHREE)
    private String isoThree;
    @Column(name = ColumnUtils.ISDCODE)
    private String isdCode;
}
