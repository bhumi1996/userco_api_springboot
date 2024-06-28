package com.secui.controller;

import com.secui.entity.ServiceEntity;
import com.secui.request.ServiceRequestDto;
import com.secui.response.ServiceResponseDto;
import com.secui.service.InitBinderInterface;
import com.secui.service.ServiceInterface;
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

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = UrlConstants.SETTINGS_BASE_URL + UrlConstants.SERVICE)
public class ServiceController implements InitBinderInterface {

    private final ServiceInterface serviceInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String listAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_NUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.DEFAULT_PAGE_SIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(serviceInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/service/list?page=", model);
        return PageConstants.SERVICE_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model) {
        model.addAttribute(ConstantUtil.SERVICE_DTO, new ServiceRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.SERVICE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(Model model,
                       @ModelAttribute(ConstantUtil.SERVICE_DTO) @Valid ServiceRequestDto serviceRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        // Controller side validation
        if (validateAccessibility(model, result, serviceRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.SERVICE_ADD_PAGE;
        }
        //Repository side operation
        if (serviceInterface.save(serviceRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    serviceRequestDto.getServiceName() + " added successfully");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, serviceRequestDto.getServiceName() + " add failed");
        return UrlConstants.REDIRECT_SERVICE_LIST_URL;
    }

    @GetMapping(path = UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes,
                       ModelMap model) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !serviceInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        /*
         * Repository side operation
         */
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.SERVICE_DTO, serviceInterface.findByuKey(uKey));
        return PageConstants.SERVICE_EDIT_PAGE;
    }


    @PostMapping(path = UrlConstants.EDIT_URL)
    public String edit(Model model, @ModelAttribute(ConstantUtil.SERVICE_DTO) @Valid ServiceRequestDto serviceRequestDto, BindingResult result,
                       @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !serviceInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        ServiceEntity serviceEntity = serviceInterface.findByKey(uKey);
        if (validateAccessibility(model, result, serviceRequestDto, serviceEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.SERVICE_EDIT_PAGE;
        }
        /*
         * Repository side operation
         */
        if (serviceInterface.update(serviceRequestDto, serviceEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    serviceRequestDto.getServiceName() + " updated successfully");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, serviceRequestDto.getServiceName() + " update failed");
        return UrlConstants.REDIRECT_SERVICE_LIST_URL;
    }


    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(Model model, @PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {

        /*
         * Controller side validation
         */
        if (UtilHelper.isBlankString(uKey) || !serviceInterface.existsByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid service");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        ServiceResponseDto serviceResponseDto = serviceInterface.findByuKey(uKey);
        if (serviceInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,
                    serviceResponseDto.getServiceName() + " Deleted successfully");
            return UrlConstants.REDIRECT_SERVICE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ERROR, serviceResponseDto.getServiceName() + " Deleted failed");
        return UrlConstants.REDIRECT_SERVICE_LIST_URL;
    }

    private boolean validateAccessibility(Model model, BindingResult result, ServiceRequestDto serviceRequestDto, ServiceEntity serviceEntity) {
        //Repository side validation
        if (serviceRequestDto.getStatus() != null && !serviceRequestDto.getStatus().isEmpty() &&
                !UtilHelper.status().contains(serviceRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (serviceEntity != null) {
            if ((serviceRequestDto.getServiceName() != null && !serviceRequestDto.getServiceName().isEmpty()) && !serviceEntity.getServiceName().equalsIgnoreCase(serviceRequestDto.getServiceName())
                    && serviceInterface.existsByServiceName(serviceRequestDto.getServiceName())) {
                result.rejectValue(ConstantUtil.SERVICE_NAME, ErrorUtil.EXIST_SERVICE, ErrorUtil.ERROR_SERVICE_EXIST);
            }
        } else {
            if (serviceRequestDto.getServiceName() != null && !serviceRequestDto.getServiceName().isEmpty() && serviceInterface.existsByServiceName(serviceRequestDto.getServiceName())) {
                result.rejectValue(ConstantUtil.SERVICE_NAME, ErrorUtil.EXIST_SERVICE, ErrorUtil.ERROR_SERVICE_EXIST);
            }
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            return true;
        }
        return false;
    }

}
