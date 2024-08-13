package com.secui.mvc.controller;


import com.secui.mvc.entity.EmailConfigurationEntity;
import com.secui.mvc.request.EmailConfigurationRequestDto;
import com.secui.mvc.response.EmailConfigurationResponseDto;
import com.secui.mvc.service.EmailConfigurationInterface;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.PortalInterface;
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
@RequestMapping(path = UrlConstants.SETTINGS_BASE_URL + UrlConstants.EMAIL_CONFIGURATION)
@Slf4j
public class EmailConfigurationController implements InitBinderInterface {

    private final EmailConfigurationInterface emailConfigurationInterface;
    private final PortalInterface portalInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(emailConfigurationInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/email/config/list?page=", model);
        return PageConstants.EMAIL_CONFIG_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.EMAIL_CONFIGURATION_DTO, new EmailConfigurationRequestDto());
        getInitData(model);
        return PageConstants.EMAIL_CONFIG_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.EMAIL_CONFIGURATION_DTO) @Valid EmailConfigurationRequestDto emailConfigurationRequestDto
            , BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            return PageConstants.EMAIL_CONFIG_ADD_PAGE;
        }
        if (validator(result, emailConfigurationRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            return PageConstants.EMAIL_CONFIG_ADD_PAGE;
        }
        /*
         * Repository side operation
         */

        if (emailConfigurationInterface.save(emailConfigurationRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, emailConfigurationRequestDto.getName() + " added successfully");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Email Configuration " + emailConfigurationRequestDto.getName() + "  is not added");
        return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;

    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {
        EmailConfigurationResponseDto emailConfigurationResponseDto = emailConfigurationInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailConfigurationResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email configuration doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.EMAIL_CONFIGURATION_DTO, emailConfigurationResponseDto);
        getInitData(model);
        return PageConstants.EMAIL_CONFIG_EDIT_PAGE;

    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.EMAIL_CONFIGURATION_DTO) @Valid EmailConfigurationRequestDto emailConfigurationRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {


        EmailConfigurationEntity emailConfigurationEntity = emailConfigurationInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailConfigurationEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email Configuration doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        /*
         * Controller side validation
         */
        if (validator(result, emailConfigurationRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.EMAIL_CONFIG_EDIT_PAGE;
        }
        if (emailConfigurationInterface.update(emailConfigurationRequestDto, emailConfigurationEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Email Configuration " + emailConfigurationRequestDto.getName() + " is updated successfully");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Email Configuration  " + emailConfigurationRequestDto.getName() + "  is not updated");
        return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        /*
         * Controller and Repository parameters validation
         */
        EmailConfigurationResponseDto emailConfigurationResponseDto = emailConfigurationInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailConfigurationResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email configuration doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        if (emailConfigurationInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Email Configuration " + emailConfigurationResponseDto.getName() + " is deleted successfully");
            return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Email Configuration  " + emailConfigurationResponseDto.getName() + " is not deleted");
        return UrlConstants.REDIRECT_EMAIL_CONFIGURATION_LIST_URL;
    }

    private boolean validator(BindingResult result, EmailConfigurationRequestDto emailConfigurationRequestDto, EmailConfigurationEntity emailConfigurationEntity) {
        if (!UtilHelper.status().contains(emailConfigurationRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (emailConfigurationEntity == null) {
            if (emailConfigurationInterface.existsByPortalName(emailConfigurationRequestDto.getPortalName())) {
                result.rejectValue(ConstantUtil.PORTAL_NAME, ErrorUtil.CONFIGURATION_ERROR_CODE, ErrorUtil.ERROR_CONFIGURATION_NAME_EXIST);
            }
        } else {
            if (!emailConfigurationEntity.getName().equalsIgnoreCase(emailConfigurationRequestDto.getName()) && emailConfigurationInterface.existsByName(emailConfigurationRequestDto.getName())) {
                result.rejectValue(ConstantUtil.PORTAL_NAME, ErrorUtil.CONFIGURATION_ERROR_CODE, ErrorUtil.ERROR_CONFIGURATION_NAME_EXIST);
            }
        }
        if (result.hasErrors()) {
            return true;
        }
        return false;
    }
    private void getInitData(Model model)
    {
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.BOOLEAN_LIST,UtilHelper.booleanList());
        model.addAttribute(ConstantUtil.ENCODING_LIST,UtilHelper.encodingList());
        model.addAttribute(ConstantUtil.PORT_LIST,UtilHelper.portList());
        model.addAttribute(ConstantUtil.PROTOCOL_LIST,UtilHelper.protocolList());
        model.addAttribute(ConstantUtil.PORTAL_LIST,portalInterface.getActivePortal());
    }
}
