package com.secui.mvc.controller;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.request.PrivilegeAssignDto;
import com.secui.mvc.request.PrivilegeAssignListDto;
import com.secui.mvc.response.RoleResponseDto;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.PrivilegeInterface;
import com.secui.mvc.service.RoleInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.PageConstants;
import com.secui.mvc.utility.UrlConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(UrlConstants.SETTINGS_BASE_URL + UrlConstants.PRIVILEGE)
@RequiredArgsConstructor
@Slf4j
public class PrivilegeController implements InitBinderInterface {
    private final PrivilegeInterface privilegeInterface;
    private final RoleInterface roleInterface;
    private final SessionRegistry sessionRegistry;

    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model) {
        model.addAttribute(ConstantUtil.LIST, privilegeInterface.getPrivilege());
        return PageConstants.PRIVILEGE_VIEWS_PAGE;
    }

    @GetMapping(UrlConstants.ROLE_PRIVILEGES_LIST)
    public String findAll(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {
        RoleResponseDto roleResponseDto = roleInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || roleResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        model.addAttribute(ConstantUtil.LIST, privilegeInterface.getAssignPrivilege(uKey));
        model.addAttribute(ConstantUtil.ROLE, roleInterface.findByuKey(uKey).getName());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.PRIVILEGE_ASSIGN_LIST_DTO, new PrivilegeAssignListDto());
        return PageConstants.PRIVILEGES_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ROLE_PRIVILEGES_LIST)
    public String findAll(@PathVariable(ConstantUtil.UKEY) String uKey, @ModelAttribute(ConstantUtil.PRIVILEGE_ASSIGN_LIST_DTO) PrivilegeAssignListDto privilegeAssignListDto, RedirectAttributes redirectAttributes) {
        RoleEntity roleEntity = roleInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || roleEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        List<PrivilegeAssignDto> privilegeAssignList = privilegeAssignListDto.getPrivilegeAssignList();
        if (privilegeAssignList != null && !privilegeAssignList.isEmpty() && privilegeInterface.assignPrivilegeToRole(uKey, privilegeAssignListDto)) {
            sessionValidate(roleEntity.getUsers().stream().map(UserEntity::getEmail).filter(email -> !email.isEmpty()).toList());
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Privileges successfully assigned to " + roleEntity.getName());
        } else {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Privileges are not assigned to " + roleEntity.getName());
        }
        return UrlConstants.REDIRECT_ROLE_LIST_PRIVILEGES + uKey;
    }

    public void sessionValidate(List<String> userId) {
        List<Object> list = sessionRegistry.getAllPrincipals();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                UserDetails userDetails = (UserDetails) obj;
                if (userId.contains(userDetails.getUsername())) {
                    List<SessionInformation> sessionInformationList = sessionRegistry.getAllSessions(userDetails, true);
                    if (sessionInformationList != null && !sessionInformationList.isEmpty()) {
                        for (SessionInformation sessionInformation : sessionInformationList) {
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        }
    }

}
