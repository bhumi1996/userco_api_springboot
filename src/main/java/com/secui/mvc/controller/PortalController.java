package com.secui.mvc.controller;

import com.secui.mvc.request.PortalRequestDto;
import com.secui.mvc.response.PortalResponseDto;
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
@Slf4j
@RequestMapping(value = UrlConstants.SETTINGS_BASE_URL + UrlConstants.PORTAL)
public class PortalController implements InitBinderInterface {

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
        UtilHelper.setTablePagination(portalInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/portal/list?page=", model);
        return PageConstants.PORTAL_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.PORTAL_DTO, new PortalRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.PORTAL_ADD_PAGE;
    }


    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model, @ModelAttribute(ConstantUtil.PORTAL_DTO) @Valid PortalRequestDto portalRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        //Validation
        if (validatePortal(model, result, portalRequestDto, null)) {
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.PORTAL_ADD_PAGE;
        }
        //Save
        if (portalInterface.save(portalRequestDto)) {
            redirectAttributes.addFlashAttribute(UtilHelper.SUCCESS, portalRequestDto.getShortName() + ConstantUtil.PORTAL_ADD_SUCCESS);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(UtilHelper.ERROR, portalRequestDto.getShortName() + ConstantUtil.PORTAL_ADD_FAILED);
        return UrlConstants.REDIRECT_PORTAL_LIST_URL;
    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes, Model model) {
        // Controller side validation
        PortalResponseDto portalResponseDto = portalInterface.findByuKey(uKey);
        if (UtilHelper.isBlankString(uKey) || portalResponseDto == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, ConstantUtil.PORTAL_INVALID_ID);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        // Repository side operation.
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.PORTAL_DTO, portalResponseDto);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.PORTAL_EDIT_PAGE;
    }


    @PostMapping(path = UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.PORTAL_DTO) @Valid PortalRequestDto portalRequestDto, BindingResult result, @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        /*
         * Controller side validation
         */
        PortalResponseDto portalResponseDto = portalInterface.findByuKey(uKey);
        if (UtilHelper.isBlankString(uKey) || portalResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, ConstantUtil.PORTAL_INVALID_ID);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        portalRequestDto.setLogoUrl(portalResponseDto.getLogoUrl());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        if (validatePortal(model, result, portalRequestDto, portalResponseDto)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.PORTAL_EDIT_PAGE;
        }
        /*
         * Repository side operation
         */
        if (portalInterface.update(portalRequestDto, uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, portalRequestDto.getShortName() + ConstantUtil.PORTAL_UPDATE_SUCCESS);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, portalRequestDto.getShortName() + ConstantUtil.PORTAL_UPDATE_FAILED);
        return UrlConstants.REDIRECT_PORTAL_LIST_URL;
    }


    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        PortalResponseDto portalResponseDto = portalInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || portalResponseDto == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, ConstantUtil.PORTAL_INVALID_ID);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        /*
         * Repository side operation
         */
        if (portalInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(UtilHelper.SUCCESS, portalResponseDto.getShortName() + ConstantUtil.PORTAL_DELETE_SUCCESS);
            return UrlConstants.REDIRECT_PORTAL_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(UtilHelper.ERROR, "Sorry, " + portalResponseDto.getShortName() + ConstantUtil.PORTAL_DELETE_FAILED);
        return UrlConstants.REDIRECT_PORTAL_LIST_URL;
    }


    /*
    Logo Validation
     */
//    private void validateLogo(PortalRequestDto portalRequestDto, BindingResult result) {
//        String contentType = portalRequestDto.getLogo().getContentType();
//        long size = portalRequestDto.getLogo().getSize();
//        if (contentType != null && !UtilHelper.isValidImageFormat(Objects.requireNonNull(FilenameUtils.getExtension(portalRequestDto.getLogo().getOriginalFilename())))) {
//            result.rejectValue(ConstantUtil.LOGO, ErrorUtils.ERROR_IMAGE_TYPE, ErrorUtils.ERROR_IMAGE_TYPE_MSG);
//        } else if (size > 5242880) {
//            result.rejectValue(ConstantUtil.LOGO, ErrorUtils.ERROR_IMAGE_SIZE, ErrorUtils.ERROR_IMAGE_SIZE_MSG);
//        }
//    }

    private boolean validatePortal(Model model, BindingResult result, PortalRequestDto portalRequestDto, PortalResponseDto portalResponseDto) {
        // Controller side validation

        if (portalRequestDto.getStatus() != null && !portalRequestDto.getStatus().isEmpty() &&
                !UtilHelper.status().contains(portalRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);

        }
        if (portalResponseDto == null) {
//            if (portalRequestDto.getLogo() == null || portalRequestDto.getLogo().isEmpty()) {
//                result.rejectValue(ConstantUtil.LOGO, ErrorUtils.ERROR_LOGO, ErrorUtils.ERROR_LOGO_REQUIRED);
//            } else {
//                validateLogo(portalRequestDto, result);
//            }
            if (portalRequestDto.getPortalName() != null && !portalRequestDto.getPortalName().isEmpty() && portalInterface.existsByPortalName(portalRequestDto.getPortalName())) {
                result.rejectValue(ConstantUtil.PORTAL_NAME, ErrorUtil.ERROR_EXIST_PORTAL_NAME, ErrorUtil.ERROR_PORTAL_NAME_EXIST);

            }
            if (portalRequestDto.getShortName() != null && !portalRequestDto.getShortName().isEmpty() && portalInterface.existsByShortName(portalRequestDto.getShortName())) {
                result.rejectValue(ConstantUtil.SHORT_NAME, ErrorUtil.ERROR_EXIST_SHORT_NAME, ErrorUtil.ERROR_SHORT_NAME_EXIST);

            }
            if (portalRequestDto.getDomainName() != null && !portalRequestDto.getDomainName().isEmpty() && portalInterface.existsByDomainName(portalRequestDto.getDomainName())) {
                result.rejectValue(ConstantUtil.DOMAIN_NAME, ErrorUtil.ERROR_EXIST_DOMAIN_NAME, ErrorUtil.ERROR_DOMAIN_NAME_EXIST);

            }
        } else {
//            if (portalRequestDto.getLogo() != null && !portalRequestDto.getLogo().isEmpty()) {
//                validateLogo(portalRequestDto, result);
//            }
            if ((portalRequestDto.getPortalName() != null && !portalRequestDto.getPortalName().isEmpty()) && (!portalResponseDto.getPortalName().equalsIgnoreCase(portalRequestDto.getPortalName())) && portalInterface.existsByPortalName(portalRequestDto.getPortalName())) {
                result.rejectValue(ConstantUtil.PORTAL_NAME, ErrorUtil.ERROR_EXIST_PORTAL_NAME, ErrorUtil.ERROR_PORTAL_NAME_EXIST);

            }

            if ((portalRequestDto.getShortName() != null && !portalRequestDto.getShortName().isEmpty()) && !portalResponseDto.getShortName().equalsIgnoreCase(portalRequestDto.getShortName()) && portalInterface.existsByShortName(portalRequestDto.getShortName())) {
                result.rejectValue(ConstantUtil.SHORT_NAME, ErrorUtil.ERROR_EXIST_SHORT_NAME, ErrorUtil.ERROR_SHORT_NAME_EXIST);

            }
            if ((portalRequestDto.getDomainName() != null && !portalRequestDto.getDomainName().isEmpty()) && !portalResponseDto.getDomainName().equalsIgnoreCase(portalRequestDto.getDomainName()) && portalInterface.existsByDomainName(portalRequestDto.getDomainName())) {
                result.rejectValue(ConstantUtil.DOMAIN_NAME, ErrorUtil.ERROR_EXIST_DOMAIN_NAME, ErrorUtil.ERROR_DOMAIN_NAME_EXIST);
            }
        }
        if (result.hasErrors()) {
            model.addAttribute(UtilHelper.ERRORS, result);
            return true;
        }
        return false;
    }

}
