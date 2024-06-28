package com.secui.entity;

import com.secui.utility.ColumnUtils;
import com.secui.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = TableUtility.MODULE_MSTR_TBL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleEntity extends Auditable implements Serializable {

    @Column(name = ColumnUtils.NAME)
    private String name;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = ColumnUtils.MODULE_PRIVILEGES,
            joinColumns = @JoinColumn(
                    name = ColumnUtils.MODULE_PID, referencedColumnName = ColumnUtils.PID),
            inverseJoinColumns = @JoinColumn(
                    name = ColumnUtils.PRIVILEGE_PID, referencedColumnName = ColumnUtils.PID))
    private Set<PrivilegeEntity> privilegeAccess;
}
