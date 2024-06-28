package com.secui.controller;

import com.secui.entity.CampaignEntity;
import com.secui.request.CampaignRequestDto;
import com.secui.request.GroupListDto;
import com.secui.response.CampaignResponseDto;
import com.secui.service.CampaignInterface;
import com.secui.service.InitBinderInterface;
import com.secui.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping(path = UrlConstants.ADMIN_BASE_URL + UrlConstants.CAMPAIGN)
@RequiredArgsConstructor
public class CampaignController implements InitBinderInterface {
    private final CampaignInterface campaignInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String list(Model model,
                       @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                       @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                       @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                       @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                       @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                       @RequestParam(name = ConstantUtil.TYPE, required = false, defaultValue = StringUtils.EMPTY) String type,
                       @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(campaignInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), type, pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "null", model);
        if (UtilHelper.isBlankString(type)) {
            type = StringUtils.EMPTY;
        }
        model.addAttribute(ConstantUtil.TYPE, type);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, " /admin/campaign/list?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&status=" + status + "&type=" + type + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
        return PageConstants.CAMPAIGN_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String add(Model model, CampaignRequestDto campaignRequestDto) {
        model.addAttribute(ConstantUtil.CAMPAIGN_DTO, campaignRequestDto);
        getInitData(model);
        return PageConstants.CAMPAIGN_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String add(Model model, @ModelAttribute(ConstantUtil.CAMPAIGN_DTO) @Valid CampaignRequestDto campaignRequestDto,
                      BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            return PageConstants.CAMPAIGN_ADD_PAGE;
        }

//        if (campaignRequestDto.getType()!=null && !campaignRequestDto.getType().isEmpty() && campaignTypeInterface.existsByNameAndStatus(campaignRequestDto.getType(), ConstantUtil.ACTIVE)) {
//            result.rejectValue(ConstantUtil.TYPE,ErrorUtil.ERROR_INVALID_TYPE, ConstantUtil.ERROR_TYPE_INVALID_MSG);
//            model.addAttribute(ConstantUtil.ERRORS, result);
//            campaignRequestDto.setPortalName(portalName);
//            getInitData(model, portalName);
//            return PageConstants.ADD_CAMPAIGN_PAGE;
//        }
        if (!UtilHelper.status().contains(campaignRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
            model.addAttribute(UtilHelper.ERRORS, result);
            getInitData(model);
            return PageConstants.CAMPAIGN_ADD_PAGE;
        }
        if (campaignInterface.existByName(campaignRequestDto.getName())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ERROR_NAME_EXIST, ErrorUtil.ERROR_NAME_EXIST_MSG);
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            return PageConstants.CAMPAIGN_ADD_PAGE;
        }
        if (campaignInterface.save(campaignRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Campaign " + campaignRequestDto.getName() + " added successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Campaign " + campaignRequestDto.getName() + " not added successfully  ");
        return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
    }

    @GetMapping(UrlConstants.EDIT_URL)
    @PreAuthorize("hasAuthority('CAMPAIGN_UPDATE')")
    public String edit(Model model,
                       @PathVariable(ConstantUtil.UKEY) String uKey,
                       RedirectAttributes redirectAttributes) {
        CampaignResponseDto campaignResponseDto = campaignInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        model.addAttribute(ConstantUtil.CAMPAIGN_DTO, campaignResponseDto);
        model.addAttribute(ConstantUtil.UKEY, uKey);
        getInitData(model);
        return PageConstants.CAMPAIGN_EDIT_PAGE;
    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(Model model, @PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.CAMPAIGN_DTO) @Valid CampaignRequestDto campaignRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        CampaignEntity campaignEntity = campaignInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            getInitData(model);
            return PageConstants.CAMPAIGN_EDIT_PAGE;
        }
//
//        if (campaignRequestDto.getType()!=null && !campaignRequestDto.getType().isEmpty() && campaignTypeInterface.existsByNameAndStatus(campaignRequestDto.getType(), ConstantUtil.ACTIVE)) {
//            result.rejectValue(ConstantUtil.TYPE, ErrorUtil.ERROR_INVALID_TYPE, ConstantUtil.ERROR_TYPE_INVALID_MSG);
//            model.addAttribute(ConstantUtil.ERRORS, result);
//            campaignRequestDto.setPortalName(portalName);
//            model.addAttribute(ConstantUtil.UKEY, uKey);
//            getInitData(model, campaignRequestDto.getPortalName());
//            return PageConstants.EDIT_CAMPAIGN_PAGE;
//        }
        if (!campaignEntity.getName().equalsIgnoreCase(campaignRequestDto.getName()) && campaignInterface.existByName(campaignRequestDto.getName())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ERROR_NAME_EXIST, ErrorUtil.ERROR_NAME_EXIST_MSG);
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.CAMPAIGN_EDIT_PAGE;
        }

        if (campaignInterface.update(campaignRequestDto, campaignEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Campaign " + campaignRequestDto.getName() + " updated successfully ");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Campaign " + campaignRequestDto.getName() + " not updated successfully ");
        return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        //Repository side Validation
        CampaignResponseDto campaignResponseDto = campaignInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if (campaignInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, campaignResponseDto.getName() + "Deleted successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Campaign delete failed");
        return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
    }


    @GetMapping(UrlConstants.ASSIGN_GROUP_TO_CAMPGAIN_URL)
    public String assignGroup(Model model,
                              @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        CampaignResponseDto campaignResponseDto = campaignInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        model.addAttribute(ConstantUtil.GROUP_LIST, campaignInterface.findAllByStatus(uKey,ConstantUtil.ACTIVE));
        model.addAttribute(ConstantUtil.GROUP_LIST_DTO, new GroupListDto());
        model.addAttribute(ConstantUtil.NAME, campaignResponseDto.getName());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        return PageConstants.CAMPAIGN_ASSIGN_GROUP;
    }

    @PostMapping(UrlConstants.ASSIGN_GROUP_TO_CAMPGAIN_URL)
    public String assignGroup(Model model,
                              @PathVariable(ConstantUtil.UKEY) String uKey, @ModelAttribute(ConstantUtil.GROUP_LIST_DTO) @Valid GroupListDto groupListDto,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        CampaignEntity campaignEntity = campaignInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if(groupListDto.getGroupList()==null || groupListDto.getGroupList().isEmpty()){
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if (campaignInterface.addGroupToCampaign(groupListDto, campaignEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Groups assigned to " + campaignEntity.getName() + " successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Groups assigned to " + campaignEntity.getName() + " failed");
        return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
    }

    @PostMapping(UrlConstants.REMOVE_GROUP_FROM_CAMPGAIN_URL)
    public String removeGroup(Model model,
                              @PathVariable(ConstantUtil.UKEY) String uKey, @ModelAttribute(ConstantUtil.GROUP_LIST_DTO) @Valid GroupListDto groupListDto,
                              BindingResult result, RedirectAttributes redirectAttributes) {
        CampaignEntity campaignEntity = campaignInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || campaignEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Campaign Id");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if(groupListDto.getGroupList()==null || groupListDto.getGroupList().isEmpty()){
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        if (campaignInterface.removeGroupFromCampaign(groupListDto,campaignEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Groups removed from " + campaignEntity.getName() + " successfully");
            return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Groups removed from " + campaignEntity.getName() + " failed");
        return UrlConstants.REDIRECT_CAMPAIGN_LIST_URL;
    }


    private void getInitData(Model model) {
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
//        model.addAttribute(ConstantUtil.CAMPAIGN_TYPE_LIST, campaignTypeInterface.getActiveType());
    }
}
