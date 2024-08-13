package com.secui.mvc.controller;


import com.secui.mvc.entity.CountryEntity;
import com.secui.mvc.request.CountryRequestDto;
import com.secui.mvc.response.CountryResponseDto;
import com.secui.mvc.service.CountryInterface;
import com.secui.mvc.service.InitBinderInterface;
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
@RequestMapping(path = UrlConstants.SETTINGS_BASE_URL + UrlConstants.COUNTRY)
@Slf4j
public class CountryController implements InitBinderInterface {

    private final CountryInterface countryInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String findAll(Model model,
                          @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                          @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                          @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                          @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                          @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                          @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, null);
        UtilHelper.setTablePagination(countryInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "/admin/settings/country/list?page=", model);
        return PageConstants.COUNTRY_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String getUser(Model model) {
        model.addAttribute(ConstantUtil.COUNTRY_DTO, new CountryRequestDto());
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.COUNTRY_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@ModelAttribute(ConstantUtil.COUNTRY_DTO) @Valid CountryRequestDto countryRequestDto
            , BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        /*
         * Controller side validation
         */
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.COUNTRY_ADD_PAGE;
        }
        if (validator(result, countryRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            return PageConstants.COUNTRY_ADD_PAGE;
        }
        /*
         * Repository side operation
         */

        if (countryInterface.save(countryRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, countryRequestDto.getName() + " added successfully");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, country " + countryRequestDto.getName() + "  is not added");
        return UrlConstants.REDIRECT_COUNTRY_LIST_URL;

    }


    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey, Model model, RedirectAttributes redirectAttributes) {
        CountryResponseDto countryResponseDto = countryInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || countryResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Country doesn't exist");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.COUNTRY_DTO, countryResponseDto);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        return PageConstants.COUNTRY_EDIT_PAGE;

    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.COUNTRY_DTO) @Valid CountryRequestDto countryRequestDto,
                       BindingResult result, Model model, RedirectAttributes redirectAttributes) {


        CountryEntity countryEntity = countryInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || countryEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Role doesn't exist");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        /*
         * Controller side validation
         */
        if (validator(result, countryRequestDto, countryEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.COUNTRY_EDIT_PAGE;
        }
        if (countryInterface.update(countryRequestDto, countryEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Country " + countryRequestDto.getName() + " is updated successfully");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Country  " + countryRequestDto.getName() + "  is not updated");
        return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        /*
         * Controller and Repository parameters validation
         */
        CountryResponseDto countryResponseDto = countryInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || countryResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Country doesn't exist");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        if (countryInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Country " + countryResponseDto.getName().toUpperCase() + " is deleted successfully");
            return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, country " + countryResponseDto.getName().toUpperCase() + " is not deleted");
        return UrlConstants.REDIRECT_COUNTRY_LIST_URL;
    }

    private boolean validator(BindingResult result, CountryRequestDto countryRequestDto, CountryEntity countryEntity) {
        if (!UtilHelper.status().contains(countryRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (countryEntity == null) {
            if (countryInterface.existsByName(countryRequestDto.getName())) {
                result.rejectValue(ConstantUtil.NAME, ErrorUtil.COUNTRY_ERROR_CODE, ErrorUtil.ERROR_COUNTRY_NAME_EXIST);
            }
            if (countryInterface.existsByIsoTwo(countryRequestDto.getIsoTwo())) {
                result.rejectValue(ConstantUtil.ISOTWO, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
            if (countryInterface.existsByIsoThree(countryRequestDto.getIsoThree())) {
                result.rejectValue(ConstantUtil.ISOTHREE, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
            if (countryInterface.existsByIsdCode(countryRequestDto.getIsdCode())) {
                result.rejectValue(ConstantUtil.ISDCODE, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
        } else {
            if (!countryEntity.getName().equalsIgnoreCase(countryRequestDto.getName()) && countryInterface.existsByName(countryRequestDto.getName())) {
                result.rejectValue(ConstantUtil.NAME, ErrorUtil.COUNTRY_ERROR_CODE, ErrorUtil.ERROR_COUNTRY_NAME_EXIST);
            }
            if (!countryEntity.getIsoTwo().equalsIgnoreCase(countryRequestDto.getIsoTwo()) && countryInterface.existsByIsoTwo(countryRequestDto.getIsoTwo())) {
                result.rejectValue(ConstantUtil.ISOTWO, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
            if (!countryEntity.getIsoThree().equalsIgnoreCase(countryRequestDto.getIsoThree()) && countryInterface.existsByIsoThree(countryRequestDto.getIsoThree())) {
                result.rejectValue(ConstantUtil.ISOTHREE, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
            if (!countryEntity.getIsdCode().equalsIgnoreCase(countryRequestDto.getIsdCode()) && countryInterface.existsByIsdCode(countryRequestDto.getIsdCode())) {
                result.rejectValue(ConstantUtil.ISDCODE, ErrorUtil.ISO_CODE_EXISTS, ErrorUtil.ERROR_ISO_CODE_EXIST);
            }
        }
        if (result.hasErrors()) {
            return true;
        }
        return false;
    }
}
