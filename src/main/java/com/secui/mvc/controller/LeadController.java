package com.secui.mvc.controller;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.request.LeadRequestDto;
import com.secui.mvc.response.LeadResponseDto;
import com.secui.mvc.service.CountryInterface;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.LeadInterface;
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

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = UrlConstants.ADMIN_BASE_URL + UrlConstants.LEAD)
@Slf4j
public class LeadController implements InitBinderInterface {

    private final LeadInterface leadInterface;
    private final CountryInterface countryInterface;
    private final PortalInterface portalInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                          @RequestParam(name = ConstantUtil.STAGE, required = false, defaultValue = StringUtils.EMPTY) String stage,
                          @RequestParam(name = ConstantUtil.CHANNEL, required = false, defaultValue = StringUtils.EMPTY) String channel,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, null, null);
        UtilHelper.setTablePagination(leadInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), stage, channel, pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "null", model);
        if (UtilHelper.isBlankString(stage)) {
            stage = StringUtils.EMPTY;
        }
        if (UtilHelper.isBlankString(channel)) {
            channel = StringUtils.EMPTY;
        }
        model.addAttribute(ConstantUtil.STAGE, stage);
        model.addAttribute(ConstantUtil.CHANNEL, channel);
        model.addAttribute(ConstantUtil.STAGE_LIST, UtilHelper.leadStage());
        model.addAttribute(ConstantUtil.CHANNEL_LIST, UtilHelper.leadChannel());
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, " /admin/lead/list?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&stage=" + stage + "&channel=" + channel + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
        return PageConstants.LEAD_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String getUser(Model model) {
        model.addAttribute(ConstantUtil.LEAD_DTO, new LeadRequestDto());
        model.addAllAttributes(getLeadList());
        return PageConstants.LEAD_ADD_PAGE;
    }


    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.LEAD_DTO) @Valid LeadRequestDto leadRequestDto
            , BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getLeadList());
            return PageConstants.LEAD_ADD_PAGE;
        }
        if (validator(result, leadRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getLeadList());
            return PageConstants.LEAD_ADD_PAGE;
        }
        /*
         * Repository side operation
         */

        if (leadInterface.save(leadRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, leadRequestDto.getLeadName() + " added successfully");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, lead " + leadRequestDto.getLeadName() + "  is not added");
        return UrlConstants.REDIRECT_LEAD_LIST_URL;

    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {

        LeadResponseDto leadResponseDto = leadInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || leadResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.LEAD_DTO, leadResponseDto);
        model.addAllAttributes(getLeadList());
        return PageConstants.LEAD_EDIT_PAGE;

    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.LEAD_DTO) @Valid LeadRequestDto leadRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {


        LeadEntity leadEntity = leadInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }


        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getLeadList());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.LEAD_EDIT_PAGE;
        }

        if (validator(result, leadRequestDto, leadEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getLeadList());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.LEAD_EDIT_PAGE;
        }
        if (leadInterface.update(leadRequestDto, leadEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Lead " + leadRequestDto.getLeadName() + " is updated successfully");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, lead  " + leadRequestDto.getLeadName() + "  is not updated");
        return UrlConstants.REDIRECT_LEAD_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        /*
         * Controller and Repository parameters validation
         */
        LeadResponseDto leadResponseDto = leadInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || leadResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        if (leadInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Lead " + leadResponseDto.getLeadName() + " is deleted successfully");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, lead " + leadResponseDto.getLeadName() + " is not deleted");
        return UrlConstants.REDIRECT_LEAD_LIST_URL;
    }

    private boolean validator(BindingResult result, @Valid LeadRequestDto leadRequestDto, LeadEntity leadEntity) {
        if (!UtilHelper.leadStage().contains(leadRequestDto.getStage())) {
            result.rejectValue(ConstantUtil.STAGE, ConstantUtil.ERROR_INVALID_STAGE, ConstantUtil.ERROR_STAGE_INVALID_MSG);
        }
        if (!UtilHelper.leadChannel().contains(leadRequestDto.getChannel())) {
            result.rejectValue(ConstantUtil.CHANNEL, ConstantUtil.ERROR_INVALID_CHANNEL, ConstantUtil.ERROR_CHANNEL_INVALID_MSG);
        }
        if (!UtilHelper.gender().contains(leadRequestDto.getGender())) {
            result.rejectValue(ConstantUtil.GENDER, ConstantUtil.ERROR_INVALID_GENDER, ConstantUtil.ERROR_GENDER_INVALID_MSG);
        }
        if (!countryInterface.existsByNameAndStatus(leadRequestDto.getCountry())) {
            result.rejectValue(ConstantUtil.COUNTRY, ConstantUtil.ERROR_INVALID_COUNTRY, ConstantUtil.ERROR_COUNTRY_INVALID_MSG);
        }
        if (!countryInterface.existsByIsdCodeAndStatus(leadRequestDto.getDialCode())) {
            result.rejectValue(ConstantUtil.DIAL_CODE, ConstantUtil.ERROR_INVALID_DIALCODE, ConstantUtil.ERROR_DIALCODE_INVALID_MSG);
        }
        if (leadEntity == null) {
            if (leadInterface.existsByEmail(leadRequestDto.getEmail())) {
                result.rejectValue(ConstantUtil.EMAIL, ErrorUtil.ERROR_EMAIL_EXISTS, ErrorUtil.ERROR_EMAIL_EXIST_MSG);
            }

        } else {
            if (!leadEntity.getEmail().equalsIgnoreCase(leadRequestDto.getEmail()) && leadInterface.existsByEmail(leadRequestDto.getEmail())) {
                result.rejectValue(ConstantUtil.EMAIL, ErrorUtil.ERROR_EMAIL_EXISTS, ErrorUtil.ERROR_EMAIL_EXIST_MSG);
            }

        }
        return result.hasErrors();
    }

    private Map<String, Object> getLeadList() {
        Map<String, Object> model = new HashMap<>();
        model.put(ConstantUtil.STAGE_LIST, UtilHelper.leadStage());
        model.put(ConstantUtil.CHANNEL_LIST, UtilHelper.leadChannel());
        model.put(ConstantUtil.GENDER_LIST, UtilHelper.gender());
        model.put(ConstantUtil.COUNTRY_LIST, countryInterface.getActiveCountry());
        model.put(ConstantUtil.PORTAL_LIST,portalInterface.getActivePortal());
        return model;
    }


}
