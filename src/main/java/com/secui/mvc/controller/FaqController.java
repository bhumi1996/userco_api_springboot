package com.secui.mvc.controller;

import com.secui.mvc.entity.CampaignTypeEntity;
import com.secui.mvc.entity.FaqEntity;
import com.secui.mvc.request.CampaignTypeRequestDto;
import com.secui.mvc.request.FaqRequestDto;
import com.secui.mvc.response.CampaignTypeResponseDto;
import com.secui.mvc.response.FaqResponseDto;
import com.secui.mvc.service.FaqInterface;
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
@RequestMapping(path = UrlConstants.SETTINGS_BASE_URL + UrlConstants.FAQ)
@Slf4j
public class FaqController implements InitBinderInterface {
 private final FaqInterface faqInterface;
    @GetMapping(UrlConstants.LIST_URL)
    public String listAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(faqInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/faq/list?page=", model);
        return PageConstants.FAQ_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.FAQ_DTO, new FaqRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.FAQ_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model,
                       @ModelAttribute(ConstantUtil.FAQ_DTO) @Valid FaqRequestDto faqRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        // Controller side validation
        if (validate(model, result, faqRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.FAQ_ADD_PAGE;
        }
        //Repository side operation
        if (faqInterface.save(faqRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                     "Faq added successfully");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR,  "Faq add failed");
        return UrlConstants.REDIRECT_FAQ_LIST_URL;
    }

    @GetMapping(path = UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes,
                       ModelMap model) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !faqInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Faq");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        /*
         * Repository side operation
         */
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.FAQ_DTO, faqInterface.findByuKey(uKey));
        return PageConstants.FAQ_EDIT_PAGE;
    }


    @PostMapping(path = UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.FAQ_DTO) @Valid FaqRequestDto faqRequestDto, BindingResult result,
                       @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !faqInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        FaqEntity faqEntity = faqInterface.findByKey(uKey);
        if (validate(model, result, faqRequestDto, faqEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.FAQ_EDIT_PAGE;
        }
        /*
         * Repository side operation
         */
        if (faqInterface.update(faqRequestDto, faqEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    "Faq updated successfully");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR,  "Faq update failed");
        return UrlConstants.REDIRECT_FAQ_LIST_URL;
    }


    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(Model model, @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !faqInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        FaqResponseDto faqResponseDto = faqInterface.findByuKey(uKey);
        if (faqInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    "Faq deleted successfully");
            return UrlConstants.REDIRECT_FAQ_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR,   "Faq deleted failed");
        return UrlConstants.REDIRECT_FAQ_LIST_URL;
    }

    private boolean validate(Model model, BindingResult result, FaqRequestDto faqRequestDto, FaqEntity faqEntity) {
        //Repository side validation
        if (faqRequestDto.getStatus() != null && !faqRequestDto.getStatus().isEmpty() &&
                !UtilHelper.status().contains(faqRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (faqEntity != null) {
            if ((faqRequestDto.getFaqQuestion() != null && !faqRequestDto.getFaqQuestion().isEmpty()) && !faqEntity.getFaqQuestion().equalsIgnoreCase(faqRequestDto.getFaqQuestion())
                    && faqInterface.existsByFaqQuestion(faqRequestDto.getFaqQuestion())) {
                result.rejectValue(ColumnUtils.FAQ_QUESTION, ErrorUtil.EXIST_FAQ, ErrorUtil.ERROR_FAQ_EXIST);
            }
        } else {
            if (faqRequestDto.getFaqQuestion() != null && !faqRequestDto.getFaqQuestion().isEmpty() && faqInterface.existsByFaqQuestion(faqRequestDto.getFaqQuestion())) {
                result.rejectValue(ColumnUtils.FAQ_QUESTION, ErrorUtil.EXIST_FAQ, ErrorUtil.ERROR_FAQ_EXIST);
            }
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            return true;
        }
        return false;
    }
}
