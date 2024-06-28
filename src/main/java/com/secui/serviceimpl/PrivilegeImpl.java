package com.secui.serviceimpl;

import com.secui.entity.ModuleEntity;
import com.secui.entity.PrivilegeEntity;
import com.secui.entity.RoleEntity;
import com.secui.repository.PrivilegeRepository;
import com.secui.request.PrivilegeAssignDto;
import com.secui.request.PrivilegeAssignListDto;
import com.secui.service.ModuleInterface;
import com.secui.service.PrivilegeInterface;
import com.secui.service.RoleInterface;
import com.secui.utility.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class PrivilegeImpl implements PrivilegeInterface {
    private final PrivilegeRepository privilegeRepository;
    private final RoleInterface roleInterface;
    private final ModuleInterface moduleInterface;

    @Override
    public LinkedHashMap<String, LinkedHashMap<String, String>> getPrivilege() {
        LinkedHashMap<String, LinkedHashMap<String, String>> privilegeAccess = new LinkedHashMap<>();
        List<ModuleEntity> list = moduleInterface.findAll();
        if (list != null && !list.isEmpty()) {
            for (ModuleEntity moduleEntity : list) {
                LinkedHashMap<String, String> privilege = new LinkedHashMap<>();
                if (moduleEntity != null && moduleEntity.getPrivilegeAccess() != null
                        && !moduleEntity.getPrivilegeAccess().isEmpty()) {
                    Iterator<PrivilegeEntity> privilegeItr = moduleEntity.getPrivilegeAccess().iterator();
                    privilege.put(AuthUtil.READ, null);
                    privilege.put(AuthUtil.VIEWALL, null);
                    privilege.put(AuthUtil.CREATE, null);
                    privilege.put(AuthUtil.UPDATE, null);
                    privilege.put(AuthUtil.DELETE, null);
                    while (privilegeItr.hasNext()) {
                        PrivilegeEntity privilegeEntity = privilegeItr.next();
                        if (privilegeEntity.getName().contains(AuthUtil.READ)) {
                            privilege.put(AuthUtil.READ, AuthUtil.YES);
                        } else if (privilegeEntity.getName().contains(AuthUtil.VIEWALL)) {
                            privilege.put(AuthUtil.VIEWALL, AuthUtil.YES);
                        } else if (privilegeEntity.getName().contains(AuthUtil.CREATE)) {
                            privilege.put(AuthUtil.CREATE, AuthUtil.YES);
                        } else if (privilegeEntity.getName().contains(AuthUtil.UPDATE)) {
                            privilege.put(AuthUtil.UPDATE, AuthUtil.YES);
                        } else if (privilegeEntity.getName().contains(AuthUtil.DELETE)) {
                            privilege.put(AuthUtil.DELETE, AuthUtil.YES);
                        }  else {
                            privilege.put(AuthUtil.NA, AuthUtil.NO);
                        }
                    }
                    privilegeAccess.put(moduleEntity.getName(), privilege);
                }
            }
        }
        return privilegeAccess;
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<String, String>> getAssignPrivilege(String uKey) {
        RoleEntity roleEntity = roleInterface.findByKey(uKey);
        Set<PrivilegeEntity> rolePrivilesSet = roleEntity.getPrivileges();
        LinkedHashMap<String, LinkedHashMap<String, String>> privilegeAccess = new LinkedHashMap<>();
        List<ModuleEntity> list = moduleInterface.findAll();
        if (list != null && !list.isEmpty()) {
            for (ModuleEntity moduleEntity : list) {
                LinkedHashMap<String, String> privilege = new LinkedHashMap<>();
                if (moduleEntity != null && moduleEntity.getPrivilegeAccess() != null
                        && !moduleEntity.getPrivilegeAccess().isEmpty()) {
                    Iterator<PrivilegeEntity> privilegeItr = moduleEntity.getPrivilegeAccess().iterator();
                    privilege.put(AuthUtil.READ, null);
                    privilege.put(AuthUtil.VIEWALL, null);
                    privilege.put(AuthUtil.CREATE, null);
                    privilege.put(AuthUtil.UPDATE, null);
                    privilege.put(AuthUtil.DELETE, null);
                    while (privilegeItr.hasNext()) {
                        PrivilegeEntity privilegeEntity = privilegeItr.next();
                        if (privilegeEntity.getName().contains(AuthUtil.READ)) {
                            if (rolePrivilesSet.contains(privilegeEntity)) {
                                privilege.put(AuthUtil.READ, AuthUtil.YES);
                            } else {
                                privilege.put(AuthUtil.READ, AuthUtil.NO);
                            }
                            continue;
                        }
                        if (privilegeEntity.getName().contains(AuthUtil.VIEWALL)) {
                            if (rolePrivilesSet.contains(privilegeEntity)) {
                                privilege.put(AuthUtil.VIEWALL, AuthUtil.YES);
                            } else {
                                privilege.put(AuthUtil.VIEWALL, AuthUtil.NO);
                            }
                            continue;
                        }
                        if (privilegeEntity.getName().contains(AuthUtil.CREATE)) {
                            if (rolePrivilesSet.contains(privilegeEntity)) {
                                privilege.put(AuthUtil.CREATE, AuthUtil.YES);
                            } else {
                                privilege.put(AuthUtil.CREATE, AuthUtil.NO);
                            }
                            continue;
                        }
                        if (privilegeEntity.getName().contains(AuthUtil.UPDATE)) {
                            if (rolePrivilesSet.contains(privilegeEntity)) {
                                privilege.put(AuthUtil.UPDATE, AuthUtil.YES);
                            } else {
                                privilege.put(AuthUtil.UPDATE, AuthUtil.NO);
                            }
                            continue;
                        }
                        if (privilegeEntity.getName().contains(AuthUtil.DELETE)) {
                            if (rolePrivilesSet.contains(privilegeEntity)) {
                                privilege.put(AuthUtil.DELETE, AuthUtil.YES);
                            } else {
                                privilege.put(AuthUtil.DELETE, AuthUtil.NO);
                            }
                        }
                    }
                    privilegeAccess.put(moduleEntity.getName(), privilege);
                }
            }
        }
        return privilegeAccess;
    }

    @Override
    public boolean assignPrivilegeToRole(String uKey, PrivilegeAssignListDto privilegeAssignListDto) {
        RoleEntity roleEntity = roleInterface.findByKey(uKey);
        if (roleEntity != null) {
            Set<PrivilegeEntity> setPrivilegeModel = new HashSet<>();
            List<PrivilegeAssignDto> list = privilegeAssignListDto.getPrivilegeAssignList();
            if (list != null && !list.isEmpty()) {
                for (PrivilegeAssignDto privilegeAssignDto : list) {
                    if (privilegeAssignDto.getModule() != null && !privilegeAssignDto.getModule().isEmpty()) {
                        ModuleEntity moduleEntity = moduleInterface.findByName(privilegeAssignDto.getModule());
                        if (moduleEntity != null) {
                            Set<PrivilegeEntity> privilegeAccessSet = moduleEntity.getPrivilegeAccess();
                            for (PrivilegeEntity privilegeEntity : privilegeAccessSet) {
                                if (privilegeAssignDto.getPrivilegeRead() != null && !privilegeAssignDto.getPrivilegeRead().isEmpty() && privilegeAssignDto.getPrivilegeRead().equalsIgnoreCase("on") && privilegeEntity.getName().contains(AuthUtil.READ)) {
                                    setPrivilegeModel.add(privilegeEntity);
                                } else if (privilegeAssignDto.getPrivilegeViewAll() != null && !privilegeAssignDto.getPrivilegeViewAll().isEmpty() && privilegeAssignDto.getPrivilegeViewAll().equalsIgnoreCase("on") && privilegeEntity.getName().contains(AuthUtil.VIEWALL)) {
                                    setPrivilegeModel.add(privilegeEntity);
                                } else if (privilegeAssignDto.getPrivilegeCreate() != null && !privilegeAssignDto.getPrivilegeCreate().isEmpty() && privilegeAssignDto.getPrivilegeCreate().equalsIgnoreCase("on") && privilegeEntity.getName().contains(AuthUtil.CREATE)) {
                                    setPrivilegeModel.add(privilegeEntity);
                                } else if (privilegeAssignDto.getPrivilegeUpdate() != null && !privilegeAssignDto.getPrivilegeUpdate().isEmpty() && privilegeAssignDto.getPrivilegeUpdate().equalsIgnoreCase("on") && privilegeEntity.getName().contains(AuthUtil.UPDATE)) {
                                    setPrivilegeModel.add(privilegeEntity);
                                } else if (privilegeAssignDto.getPrivilegeDelete() != null && !privilegeAssignDto.getPrivilegeDelete().isEmpty() && privilegeAssignDto.getPrivilegeDelete().equalsIgnoreCase("on") && privilegeEntity.getName().contains(AuthUtil.DELETE)) {
                                    setPrivilegeModel.add(privilegeEntity);
                                }
                            }
                        }
                    }
                }
                roleEntity.getPrivileges().clear();
                roleEntity.setPrivileges(setPrivilegeModel);
                roleEntity.setLastModifiedBy("Bhumika");
                return roleInterface.save(roleEntity);
            }
        }
        return false;
    }

    @Override
    public PrivilegeEntity findByName(String name) {
        return privilegeRepository.findByName(name);
    }

    @Override
    public void save(PrivilegeEntity privilege) {
        privilegeRepository.save(privilege);
    }
}
