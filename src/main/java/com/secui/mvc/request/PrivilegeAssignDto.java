package com.secui.mvc.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeAssignDto {

    private String module;
    private String privilegeRead;
    private String privilegeViewAll;
    private String privilegeCreate;
    private String privilegeUpdate;
    private String privilegeDelete;
}
