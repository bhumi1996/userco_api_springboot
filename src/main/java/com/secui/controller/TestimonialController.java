package com.secui.controller;

import com.secui.entity.TestimonialEntity;
import com.secui.request.TestimonialRequestDto;
import com.secui.response.TestimonialResponseDto;
import com.secui.service.InitBinderInterface;
import com.secui.service.TestimonialInterface;
import com.secui.utility.*;
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

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = UrlConstants.SETTINGS_BASE_URL + UrlConstants.TESTIMONIAL)
public class TestimonialController implements InitBinderInterface {

    private final TestimonialInterface testimonialInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String listAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(testimonialInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/testimonial/list?page=", model);
        return PageConstants.TESTIMONIAL_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.TESTIMONIAL_DTO, new TestimonialRequestDto());
        model.addAllAttributes(getTestimonialLists());
        return PageConstants.TESTIMONIAL_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model, @ModelAttribute(ConstantUtil.TESTIMONIAL_DTO) @Valid TestimonialRequestDto testimonialRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {

        testimonialsValidator(testimonialRequestDto, result);
        // Controller side validation
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getTestimonialLists());
            return PageConstants.TESTIMONIAL_ADD_PAGE;
        }
        //Repository side validation
        if (testimonialInterface.existByNameAndDesignation(testimonialRequestDto.getName(), testimonialRequestDto.getDesignation())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ERROR_NAME_DESIGNATION_EXITS, ErrorUtil.ERROR_NAME_DESIGNATION_EXISTS);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getTestimonialLists());
            return PageConstants.TESTIMONIAL_ADD_PAGE;
        }
        //Repository side operation
        if (testimonialInterface.save(testimonialRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    testimonialRequestDto.getName() + "added successfully");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, testimonialRequestDto.getName() + "add failed ");
        return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
    }


    @GetMapping(path = UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes,
                       ModelMap model) {


        if (UtilHelper.isBlankString(uKey) || !testimonialInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid testimonial");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        /*
         * Repository side operation
         */
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAllAttributes(getTestimonialLists());
        model.addAttribute(ConstantUtil.TESTIMONIAL_DTO, testimonialInterface.findByuKey(uKey));
        return PageConstants.TESTIMONIAL_EDIT_PAGE;
    }


    @PostMapping(path = UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.TESTIMONIAL_DTO) @Valid TestimonialRequestDto testimonialRequestDto, BindingResult result,
                       @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !testimonialInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid testimonial");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        TestimonialEntity testimonialEntity = testimonialInterface.findByKey(uKey);
        model.addAttribute(ConstantUtil.UKEY, uKey);
        testimonialRequestDto.setFileName(testimonialEntity.getFileName());
        testimonialsValidator(testimonialRequestDto, result);
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getTestimonialLists());
            return PageConstants.TESTIMONIAL_EDIT_PAGE;
        }

        if (!(testimonialEntity.getName().equalsIgnoreCase(testimonialRequestDto.getName()) &&
                testimonialEntity.getDesignation().equalsIgnoreCase(testimonialRequestDto.getDesignation())) && testimonialInterface.existByNameAndDesignation(testimonialRequestDto.getName(), testimonialRequestDto.getDesignation())) {
            result.rejectValue(ConstantUtil.NAME, ErrorUtil.ERROR_NAME_DESIGNATION_EXITS, ErrorUtil.ERROR_NAME_DESIGNATION_EXISTS);
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getTestimonialLists());
            return PageConstants.TESTIMONIAL_EDIT_PAGE;
        }

        /*
         * Repository side operation
         */
        if (testimonialInterface.update(testimonialRequestDto, testimonialEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    testimonialRequestDto.getName() + " updated successfully");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, testimonialRequestDto.getName() + " update failed ");
        return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
    }


    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(Model model, @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !testimonialInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid testimonial");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        TestimonialResponseDto testimonialResponseDto = testimonialInterface.findByuKey(uKey);
        if (testimonialInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    testimonialResponseDto.getName() + "deleted successfully");
            return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, testimonialResponseDto.getName() + "delete failed");
        return UrlConstants.REDIRECT_TESTIMONIAL_LIST_URL;
    }

    private Map<String, Object> getTestimonialLists() {
        Map<String, Object> model = new HashMap<>();
        model.put(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return model;
    }

    private void testimonialsValidator(TestimonialRequestDto testimonialRequestDto, BindingResult result) {
        if (testimonialRequestDto.getStatus() != null && !testimonialRequestDto.getStatus().isEmpty() &&
                !UtilHelper.status().contains(testimonialRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
    }

}
