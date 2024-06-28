package com.secui.service;

import com.secui.entity.PrivilegeEntity;
import com.secui.request.PrivilegeAssignListDto;

import java.util.LinkedHashMap;

public interface PrivilegeInterface {
    LinkedHashMap<String, LinkedHashMap<String, String>> getPrivilege();

    LinkedHashMap<String, LinkedHashMap<String, String>> getAssignPrivilege(String uKey);

    boolean assignPrivilegeToRole(String uKey, PrivilegeAssignListDto privilegeAssignListDto);

    PrivilegeEntity findByName(String name);

    void save(PrivilegeEntity privilege);
}
