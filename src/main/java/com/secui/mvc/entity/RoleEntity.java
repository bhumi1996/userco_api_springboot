package com.secui.mvc.entity;

import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.TableUtility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableUtility.ROLE_MSTR_TBL)
public class RoleEntity extends Auditable{

    @Column(name = ColumnUtils.NAME)
    private String name;
    @Column(name = ColumnUtils.DESCRIPTION)
    private String description;
    @ManyToMany(mappedBy = ColumnUtils.ROLES)
    private Collection<UserEntity> users;
    @ManyToMany(cascade= CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(
            name = ColumnUtils.ROLES_PRIVILEGES,
            joinColumns = @JoinColumn(
                    name = ColumnUtils.ROLE_PID, referencedColumnName = ColumnUtils.PID),
            inverseJoinColumns = @JoinColumn(
                    name = ColumnUtils.PRIVILEGE_PID, referencedColumnName = ColumnUtils.PID))
    private Set<PrivilegeEntity> privileges =new LinkedHashSet<>();
}
