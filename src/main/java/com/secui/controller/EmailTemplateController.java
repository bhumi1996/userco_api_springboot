package com.secui.controller;

import com.secui.entity.EmailTemplateEntity;
import com.secui.request.EmailTemplateRequestDto;
import com.secui.response.EmailTemplateResponseDto;
import com.secui.service.EmailTemplateInterface;
import com.secui.service.InitBinderInterface;
import com.secui.utility.*;
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
@RequestMapping(path = UrlConstants.SETTINGS_BASE_URL + UrlConstants.EMAIL_TEMPLATE)
@Slf4j
public class EmailTemplateController implements InitBinderInterface {

    private final EmailTemplateInterface emailTemplateInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.TEMPLATE, required = false, defaultValue = StringUtils.EMPTY) String template,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(emailTemplateInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), template, pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "null", model);
        if (UtilHelper.isBlankString(template)) {
            template = StringUtils.EMPTY;
        }
        model.addAttribute(ConstantUtil.TEMPLATE, template);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, " /admin/settings/email/template/list?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&status=" + status + "&template=" + template + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
        return PageConstants.TEMPLATE_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String getUser(Model model) {
        model.addAttribute(ConstantUtil.EMAIL_TEMPLATE_DTO, new EmailTemplateRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
        return PageConstants.TEMPLATE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.EMAIL_TEMPLATE_DTO) @Valid EmailTemplateRequestDto emailTemplateRequestDto
            , BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
            return PageConstants.TEMPLATE_ADD_PAGE;
        }
        if (validator(result, emailTemplateRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
            return PageConstants.TEMPLATE_ADD_PAGE;
        }
        /*
         * Repository side operation
         */

        if (emailTemplateInterface.save(emailTemplateRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, emailTemplateRequestDto.getTemplateName() + " added successfully");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, " + emailTemplateRequestDto.getTemplateName() + "  is not added");
        return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;

    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {

        EmailTemplateResponseDto emailTemplateResponseDto = emailTemplateInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailTemplateResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email template doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.EMAIL_TEMPLATE_DTO, emailTemplateResponseDto);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
        return PageConstants.TEMPLATE_EDIT_PAGE;

    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.EMAIL_TEMPLATE_DTO) @Valid EmailTemplateRequestDto emailTemplateRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {


        EmailTemplateEntity emailTemplateEntity = emailTemplateInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailTemplateEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email template doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.TEMPLATE_EDIT_PAGE;
        }
        if (validator(result, emailTemplateRequestDto, emailTemplateEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.TEMPLATE_LIST, UtilHelper.templates());
            return PageConstants.TEMPLATE_EDIT_PAGE;
        }
        if (emailTemplateInterface.update(emailTemplateRequestDto, emailTemplateEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, emailTemplateRequestDto.getTemplateName() + " is updated successfully");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, " + emailTemplateRequestDto.getTemplateName() + "  is not updated");
        return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        /*
         * Controller and Repository parameters validation
         */
        EmailTemplateResponseDto emailTemplateResponseDto = emailTemplateInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || emailTemplateResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Email template doesn't exist");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        if (emailTemplateInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, emailTemplateResponseDto.getTemplateName() + " is deleted successfully");
            return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, " + emailTemplateResponseDto.getTemplateName() + " is not deleted");
        return UrlConstants.REDIRECT_EMAIL_TEMPLATE_LIST_URL;
    }

    private boolean validator(BindingResult result, EmailTemplateRequestDto emailTemplateRequestDto, EmailTemplateEntity emailTemplateEntity) {
        if (!UtilHelper.status().contains(emailTemplateRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.ERROR_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (!UtilHelper.status().contains(emailTemplateRequestDto.getStatus())) {
            result.rejectValue(ColumnUtils.TEMPLATE_NAME, ConstantUtil.INVALID_TEMPLATE, ConstantUtil.ERROR_TEMPLATE_INVALID_MSG);
        }
        if (emailTemplateEntity == null) {
            if (emailTemplateInterface.existsByTemplateName(emailTemplateRequestDto.getTemplateName())) {
                result.rejectValue(ColumnUtils.TEMPLATE_NAME, ErrorUtil.ERROR_TEMPLATE_EXISTS, ErrorUtil.ERROR_TEMPLATE_EXIST_MSG);
            }
        } else {
            if (!emailTemplateEntity.getTemplateName().equalsIgnoreCase(emailTemplateRequestDto.getTemplateName()) && emailTemplateInterface.existsByTemplateName(emailTemplateRequestDto.getTemplateName())) {
                result.rejectValue(ColumnUtils.TEMPLATE_NAME, ErrorUtil.ERROR_TEMPLATE_EXISTS, ErrorUtil.ERROR_TEMPLATE_EXIST_MSG);
            }
        }
        return result.hasErrors();
    }

}


