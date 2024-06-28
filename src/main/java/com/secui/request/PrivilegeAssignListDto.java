package com.secui.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PrivilegeAssignListDto {

    private List<PrivilegeAssignDto> privilegeAssignList;

    public void setPrivilegeAssignList(List<PrivilegeAssignDto> privilegeAssignList) {
        this.privilegeAssignList = privilegeAssignList;
    }

    @Override
    public String toString() {
        return "PrivilegeAssignListDto{" +
                "privilegeAssignList=" + privilegeAssignList +
                '}';
    }
}
