package com.secui.mvc.controller;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.request.UserRequestDto;
import com.secui.mvc.request.UserUpdateRequestDto;
import com.secui.mvc.response.UserResponseDto;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.RoleInterface;
import com.secui.mvc.service.UserInterface;
import com.secui.mvc.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(UrlConstants.SETTINGS_BASE_URL + UrlConstants.USER)
@Slf4j
public class UserController implements InitBinderInterface {
    private final UserInterface userInterface;
    private final RoleInterface roleInterface;
    private final SessionRegistry sessionRegistry;




    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(userInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/user/list?page=", model);
        return PageConstants.USER_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
        model.addAttribute(ConstantUtil.USER_REQUEST_DTO, new UserRequestDto());
        return PageConstants.USER_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.USER_REQUEST_DTO) @Valid UserRequestDto userRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // Controller side validation
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
            return PageConstants.USER_ADD_PAGE;
        }
        //User already exists check with email
        if (userInterface.existsByEmail(userRequestDto.getEmail())) {
            result.rejectValue(ConstantUtil.EMAIL, ErrorUtil.ERROR_EMAIL, ErrorUtil.EMAIL_EXIST_ERROR);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
            return PageConstants.USER_ADD_PAGE;
        }
        // Controller side password and confirm password matching validation
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            result.rejectValue(ConstantUtil.CONFIRM_PASSWORD, ConstantUtil.PASSWORD_MATCH_ERROR_CODE, ConstantUtil.PASSWORD_MISMATCH_ERROR);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
            return PageConstants.USER_ADD_PAGE;
        }
        // Repository side operation
        Set<RoleEntity> roles = new HashSet<>();
        for (String roleuKey : userRequestDto.getRoleModelList()) {
            RoleEntity roleEntity = roleInterface.findByKey(roleuKey);
            roles.add(roleEntity);
        }
        userRequestDto.setRoles(roles);
        userRequestDto.setCreatedBy("Bhumika");
        userRequestDto.setLastModifiedBy("Bhumika");
        if (userInterface.save(userRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "User " + userRequestDto.getFullName() + " is added successfully");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, User " + userRequestDto.getFullName() + " is not added");
        return UrlConstants.REDIRECT_USER_LIST_URL;

    }

    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model,
                       RedirectAttributes redirectAttributes) {
        UserResponseDto userResponseDto = userInterface.findByuKey(uKey);
        if (uKey == null && uKey.isEmpty() || userResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "User id is invalid");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        List<String> assignedRoles = userResponseDto.getRoles().stream().map(RoleEntity::getUKey).collect(Collectors.toList());
        userResponseDto.setRoleModelList(assignedRoles);
        userResponseDto.setConfirmPassword(userResponseDto.getPassword());
        model.addAttribute(ConstantUtil.USER_REQUEST_DTO, userResponseDto);
        model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        return PageConstants.USER_EDIT_PAGE;
    }

    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.USER_REQUEST_DTO) @Valid UserUpdateRequestDto userUpdateRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        UserResponseDto userResponseDto = userInterface.findByuKey(uKey);
        if (uKey == null && uKey.isEmpty() || userResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "User id is invalid");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        // Controller side validation
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.USER_EDIT_PAGE;
        }
        //  Repository side Email validation
        if (!userResponseDto.getEmail().equals(userUpdateRequestDto.getEmail()) && userInterface.existsByEmail(userUpdateRequestDto.getEmail())) {
            result.rejectValue(ConstantUtil.EMAIL, ErrorUtil.ERROR_EMAIL, ErrorUtil.EMAIL_EXIST_ERROR);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            model.addAttribute(ConstantUtil.LIST, roleInterface.findAll());
            return PageConstants.USER_EDIT_PAGE;
        }
        //  Repository side operation
        Set<RoleEntity> roles = new HashSet<>();
        if (userUpdateRequestDto.getRoleModelList() != null && !userUpdateRequestDto.getRoleModelList().isEmpty()) {
            for (String user : userUpdateRequestDto.getRoleModelList()) {
                RoleEntity roleModel = roleInterface.findByKey(user);
                roles.add(roleModel);
            }
        }
        userUpdateRequestDto.setRoles(roles);
        userUpdateRequestDto.setLastModifiedBy("Bhumika");
        if (userInterface.update(userUpdateRequestDto, uKey)) {
            sessionValidate(userResponseDto.getEmail());
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "User " + userUpdateRequestDto.getFullName() + " is updated successfully");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, User " + userUpdateRequestDto.getFullName() + " is not updated");
        return UrlConstants.REDIRECT_USER_LIST_URL;

    }
    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        UserEntity userEntity = userInterface.findByKey(uKey);
        // Controller parameters validation
        if (uKey == null || uKey.isEmpty() || userEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "User does not exist");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        if (userInterface.deleteByuKey(userEntity)) {
            sessionValidate(userEntity.getEmail());
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "User deleted successfully");
            return UrlConstants.REDIRECT_USER_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "User delete failed");
        return UrlConstants.REDIRECT_USER_LIST_URL;
    }
    public void sessionValidate(String email) {
        List<Object> list = sessionRegistry.getAllPrincipals();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                UserDetails userDetails = (UserDetails) obj;
                if (userDetails.getUsername().equalsIgnoreCase(email)) {
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
