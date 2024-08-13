package com.secui.mvc.controller;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.request.RoleRequestDto;
import com.secui.mvc.response.RoleResponseDto;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.RoleInterface;
import com.secui.mvc.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = UrlConstants.SETTINGS_BASE_URL + UrlConstants.ROLE)
@Slf4j
public class RoleController implements InitBinderInterface {

    private  final RoleInterface roleInterface;
    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(roleInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/role/list?page=", model);
        return PageConstants.ROLE_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String getUser(Model model) {
        model.addAttribute(ConstantUtil.ROLE_DTO, new RoleRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.ROLE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.ROLE_DTO) @Valid RoleRequestDto roleRequestDto
            , BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        /*
         * Controller side validation
         */
        System.out.println(result.toString());
        if (result.hasErrors()) {
            System.out.println(result.toString());
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.ROLE_ADD_PAGE;
        }
        /*
         * Repository side validation
         */
        if (roleInterface.existsByName(ConstantUtil.ROLE_PREFIX + roleRequestDto.getName())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ROLE_ERROR_CODE, ErrorUtil.ERROR_ROLE_NAME_EXIST);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.ROLE_ADD_PAGE;
        }
        if (roleRequestDto.getStatus()!=null && !roleRequestDto.getStatus().isEmpty() && !UtilHelper.status().contains(roleRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.ERROR_INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.ERRORS, result);
            return PageConstants.ROLE_ADD_PAGE;
        }
        /*
         * Repository side operation
         */
        String name = ConstantUtil.ROLE_PREFIX + roleRequestDto.getName().toUpperCase();
        roleRequestDto.setName(name);
        if (roleInterface.save(roleRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Role " + roleRequestDto.getName() + " is added successfully");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Role " + roleRequestDto.getName() + "  is not added");
        return UrlConstants.REDIRECT_LIST_ROLE_URL;

    }

    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {
       RoleResponseDto roleResponseDto = roleInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || roleResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        String[] role = roleResponseDto.getName().split("_");
        roleResponseDto.setName(role[role.length-1].toLowerCase());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.ROLE_DTO, roleResponseDto);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.ROLE_EDIT_PAGE;

    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.ROLE_DTO) @Valid RoleRequestDto roleRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {
      RoleEntity roleEntity = roleInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || roleEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.ROLE_EDIT_PAGE;
        }
        if (!UtilHelper.status().contains(roleRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.ERROR_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.ERRORS, result);
            return PageConstants.ROLE_ADD_PAGE;
        }
        /*
         * Repository side validation
         */
        if (!roleEntity.getName().equalsIgnoreCase(ConstantUtil.ROLE_PREFIX + roleRequestDto.getName()) && roleInterface.existsByName(ConstantUtil.ROLE_PREFIX + roleRequestDto.getName())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ROLE_ERROR_CODE, ErrorUtil.ERROR_ROLE_NAME_EXIST);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.ROLE_EDIT_PAGE;
        }

        /*
         * Repository side operation
         */
        String name = "ROLE_" + roleRequestDto.getName().toUpperCase();
        roleRequestDto.setName(name);
        if (roleInterface.update(roleRequestDto, roleEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Role " + roleRequestDto.getName() + " is updated successfully");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Role  " + roleRequestDto.getName() + "  is not updated");
        return UrlConstants.REDIRECT_LIST_ROLE_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        /*
         * Controller and Repository parameters validation
         */
        RoleResponseDto roleResponseDto = roleInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || roleResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        if (roleInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Role " + roleResponseDto.getName().toUpperCase() + " is deleted successfully");
            return UrlConstants.REDIRECT_LIST_ROLE_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Role " + roleResponseDto.getName().toUpperCase() + " is not deleted");
        return UrlConstants.REDIRECT_LIST_ROLE_URL;
    }

}
