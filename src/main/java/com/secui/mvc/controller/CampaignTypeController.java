package com.secui.mvc.controller;

import com.secui.mvc.entity.CampaignTypeEntity;
import com.secui.mvc.request.CampaignTypeRequestDto;
import com.secui.mvc.response.CampaignTypeResponseDto;
import com.secui.mvc.service.CampaignTypeInterface;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = UrlConstants.SETTINGS_BASE_URL + UrlConstants.CAMPAIGN_TYPE)
public class CampaignTypeController implements InitBinderInterface {

    private final CampaignTypeInterface campaignTypeInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String listAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(campaignTypeInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/campaign/type/list?page=", model);
        return PageConstants.CAMPAIGN_TYPE_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.CAMPAIGN_TYPE_DTO, new CampaignTypeRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.CAMPAIGN_TYPE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model,
                       @ModelAttribute(ConstantUtil.CAMPAIGN_TYPE_DTO) @Valid CampaignTypeRequestDto campaignTypeRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        // Controller side validation
        if (validate(model, result, campaignTypeRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.CAMPAIGN_TYPE_ADD_PAGE;
        }
        //Repository side operation
        if (campaignTypeInterface.save(campaignTypeRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    campaignTypeRequestDto.getType() + " added successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, campaignTypeRequestDto.getType() + " add failed");
        return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
    }

    @GetMapping(path = UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes,
                       ModelMap model) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !campaignTypeInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid campaign type");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        /*
         * Repository side operation
         */
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.CAMPAIGN_TYPE_DTO, campaignTypeInterface.findByuKey(uKey));
        return PageConstants.CAMPAIGN_TYPE_EDIT_PAGE;
    }


    @PostMapping(path = UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.CAMPAIGN_TYPE_DTO) @Valid CampaignTypeRequestDto campaignTypeRequestDto, BindingResult result,
                       @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !campaignTypeInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        CampaignTypeEntity campaignTypeEntity = campaignTypeInterface.findByKey(uKey);
        if (validate(model, result, campaignTypeRequestDto, campaignTypeEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.CAMPAIGN_TYPE_EDIT_PAGE;
        }
        /*
         * Repository side operation
         */
        if (campaignTypeInterface.update(campaignTypeRequestDto, campaignTypeEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    campaignTypeRequestDto.getType() + " updated successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, campaignTypeRequestDto.getType() + " update failed");
        return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
    }


    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(Model model, @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !campaignTypeInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        CampaignTypeResponseDto campaignTypeResponseDto = campaignTypeInterface.findByuKey(uKey);
        if (campaignTypeInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    campaignTypeResponseDto.getType() + " Deleted successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, campaignTypeResponseDto.getType() + " Deleted failed");
        return UrlConstants.REDIRECT_CAMPAIGN_TYPE_LIST_URL;
    }

    private boolean validate(Model model, BindingResult result, CampaignTypeRequestDto CampaignTypeRequestDto, CampaignTypeEntity CampaignTypeEntity) {
        //Repository side validation
        if (CampaignTypeRequestDto.getStatus() != null && !CampaignTypeRequestDto.getStatus().isEmpty() &&
                !UtilHelper.status().contains(CampaignTypeRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (CampaignTypeEntity != null) {
            if ((CampaignTypeRequestDto.getType() != null && !CampaignTypeRequestDto.getType().isEmpty()) && !CampaignTypeEntity.getType().equalsIgnoreCase(CampaignTypeRequestDto.getType())
                    && campaignTypeInterface.existsByType(CampaignTypeRequestDto.getType())) {
                result.rejectValue(ConstantUtil.TYPE, ErrorUtil.EXIST_TYPE, ErrorUtil.ERROR_TYPE_EXIST);
            }
        } else {
            if (CampaignTypeRequestDto.getType() != null && !CampaignTypeRequestDto.getType().isEmpty() && campaignTypeInterface.existsByType(CampaignTypeRequestDto.getType())) {
                result.rejectValue(ConstantUtil.TYPE, ErrorUtil.EXIST_TYPE, ErrorUtil.ERROR_TYPE_EXIST);
            }
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            return true;
        }
        return false;
    }
}
