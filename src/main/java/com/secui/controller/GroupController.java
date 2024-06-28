package com.secui.controller;

import com.secui.entity.GroupEntity;
import com.secui.request.GroupLeadsListDto;
import com.secui.request.GroupRequestDto;
import com.secui.response.GroupResponseDto;
import com.secui.response.LeadResponseDto;
import com.secui.service.GroupInterface;
import com.secui.service.InitBinderInterface;
import com.secui.service.LeadInterface;
import com.secui.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = UrlConstants.ADMIN_BASE_URL + UrlConstants.GROUP)
@Slf4j
public class GroupController implements InitBinderInterface {
    private static final String INVALID_ID = " Sorry, invalid group ";
    private static final String ADD_SUCCESS = " group is added successfully";
    private static final String ADD_FAILED = " group is not added";
    private static final String UPDATE_SUCCESS = " group is updated successfully";
    private static final String UPDATE_FAILED = " group is not updated";
    private static final String DELETE_SUCCESS = " group is deleted successfully";
    private static final String DELETE_FAILED = " group is not deleted";


    private final GroupInterface groupInterface;
    private final LeadInterface leadInterface;


    @GetMapping(UrlConstants.LIST_URL)
    public String list(Model model,
                       @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                       @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                       @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = "") String status,
                       @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = "") String sortBy,
                       @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = "") String sortDir,
                       @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(groupInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/group/list?page=", model);
        return PageConstants.GROUP_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.GROUP_DTO, new GroupRequestDto());
        model.addAllAttributes(getGroupLists());
        return PageConstants.GROUP_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model, @ModelAttribute(ConstantUtil.GROUP_DTO) @Valid GroupRequestDto groupRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        // Controller side validation
        if (result.hasErrors()) {
            model.addAllAttributes(getGroupLists());
            model.addAttribute(ConstantUtil.ERROR, result);
            return PageConstants.GROUP_ADD_PAGE;
        }
        if (!UtilHelper.status().contains(groupRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getGroupLists());
            return PageConstants.GROUP_ADD_PAGE;
        }
        //Repository side validation
        if (groupInterface.existsByGroupName(groupRequestDto.getGroupName())) {
            result.rejectValue(ConstantUtil.GROUP_NAME, ErrorUtil.EXIST_GROUP, ErrorUtil.ERROR_GROUP_MSG);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getGroupLists());
            return PageConstants.GROUP_ADD_PAGE;
        }
        //Repository side operation
        if (groupInterface.save(groupRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    groupRequestDto.getGroupName() + ADD_SUCCESS);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, groupRequestDto.getGroupName() + ADD_FAILED);
        return UrlConstants.REDIRECT_GROUP_LIST_URL;
    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes,
                       Model model) {
        GroupResponseDto groupResponseDto = groupInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.GROUP_NAME, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        model.addAttribute(ConstantUtil.GROUP_DTO, groupResponseDto);
        model.addAllAttributes(getGroupLists());
        return PageConstants.GROUP_EDIT_PAGE;

    }

    @PreAuthorize("hasAuthority('GROUP_UPDATE')")
    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.GROUP_DTO) @Valid GroupRequestDto groupRequestDto,
                       BindingResult result,
                       @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        GroupEntity groupEntity = groupInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || groupEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        // Controller side validation
        if (result.hasErrors()) {
            model.addAllAttributes(getGroupLists());
            model.addAttribute(ConstantUtil.ERRORS, result);
            return PageConstants.GROUP_EDIT_PAGE;
        }
        if (!UtilHelper.status().contains(groupRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
            model.addAttribute(UtilHelper.ERRORS, result);
            model.addAllAttributes(getGroupLists());
            return PageConstants.GROUP_EDIT_PAGE;
        }
        if (!groupEntity.getGroupName().equalsIgnoreCase(groupRequestDto.getGroupName())
                && groupInterface.existsByGroupName(groupRequestDto.getGroupName())) {
            result.rejectValue(ConstantUtil.GROUP_NAME, ErrorUtil.EXIST_GROUP, ErrorUtil.ERROR_GROUP_MSG);
            model.addAttribute(UtilHelper.ERRORS, result);
            model.addAllAttributes(getGroupLists());
            return PageConstants.GROUP_EDIT_PAGE;
        }
        /*
         * Repository side operation
         */
        if (groupInterface.update(groupRequestDto, groupEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    groupRequestDto.getGroupName() + UPDATE_SUCCESS);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, groupRequestDto.getGroupName() + UPDATE_FAILED);
        return UrlConstants.REDIRECT_GROUP_LIST_URL;

    }

    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        GroupEntity groupEntity = groupInterface.findByKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupEntity == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        /*
         * Repository side operation
         */
        if (groupInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(UtilHelper.SUCCESS, groupEntity.getGroupName() + DELETE_SUCCESS);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(UtilHelper.ERROR, "Sorry, " + groupEntity.getGroupName() + DELETE_FAILED);
        return UrlConstants.REDIRECT_GROUP_LIST_URL;
    }


    @GetMapping(UrlConstants.ADD_GROUP_LEADS_URL)
    public String addLeads(Model model,
                           @PathVariable(ConstantUtil.UKEY) String uKey,
                           @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                           @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                           @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                           @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = ConstantUtil.DEFAULT_SORT_DIRECTION) String sortDir,
                           @RequestParam(value = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search,
                           RedirectAttributes redirectAttributes) {
        GroupResponseDto groupResponseDto = groupInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupResponseDto == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, null, null);
        UtilHelper.setTablePagination(leadInterface.findAllPortalLeads(uKey, UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), null, pagination.get(ConstantUtil.SEARCH), "null", model);
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.GROUP_NAME, groupResponseDto.getGroupName());
        model.addAttribute(ConstantUtil.GROUP_LEAD_LIST_DTO, new GroupLeadsListDto());
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, "/admin/group/" + uKey + UrlConstants.LEAD_ADD + "?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
        return PageConstants.ADD_GROUP_LEAD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_GROUP_LEADS_URL)
    public String groupLeads(@PathVariable(ConstantUtil.UKEY) String uKey,
                             @ModelAttribute(ConstantUtil.GROUP_LEAD_LIST_DTO) @Valid GroupLeadsListDto groupLeadsListDto,
                             RedirectAttributes redirectAttributes) {
        GroupResponseDto groupResponseDto = groupInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupResponseDto == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        if (leadInterface.addLeadsToGroup(groupLeadsListDto, uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Leads successfully added to group " + groupResponseDto.getGroupName());
            return UrlConstants.REDIRECT_GROUP_BASE + uKey + UrlConstants.LEAD_ADD;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Leads adding failed for group " + groupResponseDto.getGroupName());
        return UrlConstants.REDIRECT_GROUP_BASE + uKey + UrlConstants.LEAD_ADD;
    }

    @GetMapping(UrlConstants.LIST_GROUP_LEADS_URL)
    public String listLeadAll(Model model,
                              @PathVariable(ConstantUtil.UKEY) String uKey,
                              @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                              @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                              @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                              @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = ConstantUtil.DEFAULT_SORT_DIRECTION) String sortDir,
                              @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search,
                              RedirectAttributes redirectAttributes) {
        GroupEntity groupEntity = groupInterface.findByKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupEntity == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, null, null);
        Map<String, Object> pages = leadInterface.findAllGroupLeads(groupEntity, UtilHelper.getPageSort(page, size, sortBy, sortDir), search);
        UtilHelper.setTablePagination(null, page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), null, pagination.get(ConstantUtil.SEARCH), "null", model);
        model.addAttribute(ConstantUtil.LIST, pages.get(ConstantUtil.LIST));
        model.addAttribute(ConstantUtil.TOTAL_PAGES, pages.get(ConstantUtil.TOTAL_PAGES));
        model.addAttribute(ConstantUtil.TOTAL_ELEMENTS, pages.get(ConstantUtil.TOTAL_ELEMENTS));
        model.addAttribute(ConstantUtil.GROUP_NAME, groupEntity.getGroupName());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, "/admin/group/" + uKey + "/lead/list?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&search=" + pagination.get(ConstantUtil.SEARCH) + "&sortDir=" + pagination.get(ConstantUtil.SORT_DIR));
        return PageConstants.LIST_LEAD_GROUP_PAGE;
    }

    @GetMapping(UrlConstants.REMOVE_GROUP_LEAD_URL)
    public String removeGroupLead(@PathVariable(ConstantUtil.UKEY) String uKey, @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                                  RedirectAttributes redirectAttributes
    ) {
        GroupResponseDto groupResponseDto = groupInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || groupResponseDto == null) {
            redirectAttributes.addFlashAttribute(UtilHelper.ERROR, INVALID_ID);
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        LeadResponseDto leadResponseDto = leadInterface.findByuKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid lead information");
            return UrlConstants.REDIRECT_GROUP_LIST_URL;
        }
        boolean response = leadInterface.removeFromGroup(uKey, leaduKey);
        if (response) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Lead " + leadResponseDto.getLeadName() + " successfully removed ");
            return UrlConstants.REDIRECT_GROUP_BASE + uKey + UrlConstants.LEAD_ADD;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Lead " + leadResponseDto.getLeadName() + " removing failed for " + groupResponseDto.getGroupName());
        return UrlConstants.REDIRECT_GROUP_BASE + uKey + UrlConstants.LEAD_ADD;
    }

    private Map<String, Object> getGroupLists() {
        Map<String, Object> model = new HashMap<>();
        model.put(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return model;
    }


}
