package com.secui.serviceimpl;


import com.secui.entity.PrivilegeEntity;
import com.secui.entity.RoleEntity;
import com.secui.entity.UserEntity;
import com.secui.utility.ConstantUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;


public class UserDetailsImpl implements UserDetails, Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -1633265153260177986L;

    private final String userName;
    public final String fullName;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final boolean tokenExpired;
    private final List<GrantedAuthority> authorities;
    private final boolean accountLocked;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final Date lockTime;
    private final Integer failedAttempt;
    public String profileImageUrl;


    public UserDetailsImpl(UserEntity userEntity) {
        this.userName = userEntity.getEmail();
        this.fullName = userEntity.getFullName();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.enabled = userEntity.isEnabled();
        this.tokenExpired = userEntity.isTokenExpired();
        this.authorities = getAuthorities(userEntity.getRoles());
        this.accountLocked = userEntity.isAccountLocked();
        this.accountNonExpired = userEntity.isAccountNonExpired();
        this.credentialsNonExpired = userEntity.isCredentialsNonExpired();
        this.profileImageUrl = userEntity.getProfilePath();
        this.failedAttempt = userEntity.getFailedAttempt();
        this.lockTime = userEntity.getLockTime();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    private List<GrantedAuthority> getAuthorities(Set<RoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        for (String privilege : privileges) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(privilege);
            authoritiesList.add(grantedAuthority);
        }
        return authoritiesList;
    }

    private List<String> getPrivileges(Set<RoleEntity> roles) {
        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            if (role.getStatus().equals(ConstantUtil.ACTIVE)) {
                collection.addAll(role.getPrivileges());
            }
        }
        for (PrivilegeEntity item : collection) {
            if (item.getStatus().equals(ConstantUtil.ACTIVE)) {
                privileges.add(item.getName());
            }
        }
        for (RoleEntity roleEntity : roles) {
            if (roleEntity.getStatus().equals(ConstantUtil.ACTIVE)) {
                privileges.add(roleEntity.getName());
            }
        }
        return privileges;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return  accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return enabled == that.enabled && tokenExpired == that.tokenExpired && accountLocked == that.accountLocked && accountNonExpired == that.accountNonExpired && credentialsNonExpired == that.credentialsNonExpired && userName.equals(that.userName) && Objects.equals(fullName, that.fullName)
                && email.equals(that.email) && Objects.equals(password, that.password) && Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, fullName, email, password, enabled, tokenExpired, authorities, accountLocked, accountNonExpired, credentialsNonExpired,lockTime, failedAttempt,profileImageUrl);
    }


}
