package com.secui.mvc.entity;


import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = TableUtility.PRIVILEGE_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeEntity extends Auditable implements Serializable {

    @Column(name= ColumnUtils.NAME)
    private String name;

    @ManyToMany(mappedBy = ColumnUtils.PRIVILEGES)
    private Set<RoleEntity> roles =new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name=ColumnUtils.MODULE_ID,referencedColumnName = ColumnUtils.PID)
    private ModuleEntity module;
}
