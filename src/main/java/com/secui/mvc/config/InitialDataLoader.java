package com.secui.mvc.config;

import com.secui.mvc.entity.ModuleEntity;
import com.secui.mvc.entity.PrivilegeEntity;
import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.service.ModuleInterface;
import com.secui.mvc.service.PrivilegeInterface;
import com.secui.mvc.service.RoleInterface;
import com.secui.mvc.service.UserInterface;
import com.secui.mvc.utility.AuthUtil;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserInterface userInterface;
    private final RoleInterface roleInterface;
    private final PrivilegeInterface privilegeInterface;
    private final ModuleInterface moduleInterface;
    boolean alreadySetup = false;
    @Value("${user.masterUserName}")
    private String masterUserName;
    @Value("${user.masterPassword}")
    private String masterPassword;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        // user privilege
        PrivilegeEntity userViewAll = createPrivilegeIfNotFound(AuthUtil.USER_VIEWALL);
        PrivilegeEntity userCreate = createPrivilegeIfNotFound(AuthUtil.USER_CREATE);
        PrivilegeEntity userUpdate = createPrivilegeIfNotFound(AuthUtil.USER_UPDATE);
        PrivilegeEntity userDelete = createPrivilegeIfNotFound(AuthUtil.USER_DELETE);

        //role privilege
        PrivilegeEntity roleViewAll = createPrivilegeIfNotFound(AuthUtil.ROLES_VIEWALL);
        PrivilegeEntity roleCreate = createPrivilegeIfNotFound(AuthUtil.ROLES_CREATE);
        PrivilegeEntity roleUpdate = createPrivilegeIfNotFound(AuthUtil.ROLES_UPDATE);
        PrivilegeEntity roleDelete = createPrivilegeIfNotFound(AuthUtil.ROLES_DELETE);

        // privilege
        PrivilegeEntity privilegeViewAll = createPrivilegeIfNotFound(AuthUtil.PRIVILEGES_VIEWALL);
        PrivilegeEntity privilegeCreate = createPrivilegeIfNotFound(AuthUtil.PRIVILEGES_CREATE);
        PrivilegeEntity privilegeUpdate = createPrivilegeIfNotFound(AuthUtil.PRIVILEGES_UPDATE);
        PrivilegeEntity privilegeDelete = createPrivilegeIfNotFound(AuthUtil.PRIVILEGES_DELETE);


        //  Privileges list
        List<PrivilegeEntity> userModelPrivilegesList = Arrays.asList(userViewAll,userCreate,userUpdate,userDelete);
        List<PrivilegeEntity> roleModelPrivilegesList = Arrays.asList(roleViewAll,roleCreate,roleUpdate,roleDelete);
        List<PrivilegeEntity> privilegesModelList = Arrays.asList(privilegeViewAll,privilegeCreate,privilegeUpdate,privilegeDelete);


        //privileges set
        Set<PrivilegeEntity> userModelPrivilegesSet = new HashSet<>(userModelPrivilegesList);
        Set<PrivilegeEntity> roleModelPrivilegesSet = new HashSet<>(roleModelPrivilegesList);
        Set<PrivilegeEntity> privilegesModelPrivilegesSet = new HashSet<>(privilegesModelList);

        // create module
        createModuleIfNotFound(AuthUtil.USERS,userModelPrivilegesSet);
        createModuleIfNotFound(AuthUtil.ROLES,roleModelPrivilegesSet);
        createModuleIfNotFound(AuthUtil.PRIVILEGES,privilegesModelPrivilegesSet);

        // privileges model set to create role privileges
        Set<PrivilegeEntity> privilegeModelSet = new HashSet<>();
        privilegeModelSet.addAll(userModelPrivilegesSet);
        privilegeModelSet.addAll(roleModelPrivilegesSet);
        privilegeModelSet.addAll(privilegesModelPrivilegesSet);


        createRoleIfNotFound(ConstantUtil.ROLE_ADMIN, "Role Admin", privilegeModelSet);
        createUserIfNotFound();
        alreadySetup = true;
    }

    private void createUserIfNotFound() {
        UserEntity userEntity = userInterface.findByEmail(masterUserName);
        if (userEntity == null) {
            RoleEntity roleModel = roleInterface.findByName(ConstantUtil.ROLE_ADMIN);
            userEntity = new UserEntity();
            userEntity.setFullName(ConstantUtil.ADMINISTRATOR);
            userEntity.setEmail(masterUserName);
            userEntity.setCountryDial(StringUtils.EMPTY);
            userEntity.setMobileNo("8287469826");
            userEntity.setTc("true");
            userEntity.setPassword(new BCryptPasswordEncoder().encode(masterPassword));
            userEntity.setEnabled(true);
            userEntity.setTokenExpired(false);
            userEntity.setAccountLocked(true);
            userEntity.setAccountNonExpired(true);
            userEntity.setCredentialsNonExpired(true);
            userEntity.setLogged(false);
            userEntity.setUKey(UtilHelper.uKey());
            userEntity.setCreatedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            userEntity.setLastModifiedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            userEntity.setStatus(ConstantUtil.ACTIVE);
            userEntity.setRoles(new HashSet<>(Collections.singletonList(roleModel)));
            userInterface.save(userEntity);
        }
    }

    private void createRoleIfNotFound(String name, String fullName, Set<PrivilegeEntity> privileges) {
        RoleEntity roleModel = roleInterface.findByName(name);
        if (roleModel == null) {
            roleModel = new RoleEntity(); // setRole
            roleModel.setName(name);
            roleModel.setDescription(fullName);
            roleModel.setCreatedBy(ConstantUtil.ADMINISTRATOR);
            roleModel.setLastModifiedBy(ConstantUtil.ADMINISTRATOR);
            roleModel.setUKey(UtilHelper.uKey());
            roleModel.setStatus(ConstantUtil.ACTIVE);
            if (privileges != null && !privileges.isEmpty()) {
                roleModel.setPrivileges(privileges);
            }
            roleInterface.save(roleModel);
        }
        if (privileges != null && roleModel.getPrivileges().size() != privileges.size()) {
            roleModel.getPrivileges().clear();
            roleModel.setPrivileges(privileges);
            roleInterface.save(roleModel);
        }

    }

    private void createModuleIfNotFound(String name, Set<PrivilegeEntity> privileges) {
        ModuleEntity moduleEntity = moduleInterface.findByName(name);
        if (moduleEntity == null) {
            moduleEntity = new ModuleEntity(); // setRole
            moduleEntity.setName(name);
            moduleEntity.setCreatedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            moduleEntity.setLastModifiedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            moduleEntity.setUKey(UtilHelper.uKey());
            moduleEntity.setStatus(ConstantUtil.ACTIVE);
            moduleEntity.setPrivilegeAccess(privileges);
            moduleInterface.save(moduleEntity);
        }
        if (moduleEntity.getPrivilegeAccess().size() != privileges.size()) {
            moduleEntity.getPrivilegeAccess().clear();
            moduleEntity.setPrivilegeAccess(privileges);
            moduleInterface.save(moduleEntity);
        }
    }


    private PrivilegeEntity createPrivilegeIfNotFound(String name) {
        PrivilegeEntity privilege = privilegeInterface.findByName(name);
        if (privilege == null) {
            privilege = new PrivilegeEntity(); // set name
            privilege.setName(name);
            privilege.setCreatedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            privilege.setLastModifiedBy(ConstantUtil.CREATED_MODIFIED_BY_FID);
            privilege.setUKey(UtilHelper.uKey());
            privilege.setStatus(ConstantUtil.ACTIVE);
            privilegeInterface.save(privilege);
        }
        return privilege;
    }
}
