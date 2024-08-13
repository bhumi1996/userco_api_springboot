package com.secui.mvc.service;

import com.secui.mvc.entity.PrivilegeEntity;
import com.secui.mvc.request.PrivilegeAssignListDto;

import java.util.LinkedHashMap;

public interface PrivilegeInterface {
    LinkedHashMap<String, LinkedHashMap<String, String>> getPrivilege();

    LinkedHashMap<String, LinkedHashMap<String, String>> getAssignPrivilege(String uKey);

    boolean assignPrivilegeToRole(String uKey, PrivilegeAssignListDto privilegeAssignListDto);

    PrivilegeEntity findByName(String name);

    void save(PrivilegeEntity privilege);
}
