package com.secui.mvc.controller;

import com.secui.mvc.entity.ArticleEntity;
import com.secui.mvc.request.ArticleRequestDto;
import com.secui.mvc.response.ArticleResponseDto;
import com.secui.mvc.service.ArticleInterface;
import com.secui.mvc.service.InitBinderInterface;
import com.secui.mvc.service.PortalInterface;
import com.secui.mvc.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = UrlConstants.ADMIN_BASE_URL+ UrlConstants.ARTICLE)
public class ArticleController implements InitBinderInterface {
    private final ArticleInterface articleInterface;
    private final PortalInterface portalInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String list(Model model,
                       @RequestParam(name = ConstantUtil.PAGE, required = false, defaultValue = ConstantUtil.PAGENUMBER) int page,
                       @RequestParam(name = ConstantUtil.SIZE, required = false, defaultValue = ConstantUtil.PAGESIZE) int size,
                       @RequestParam(name = ConstantUtil.STATUS, required = false, defaultValue = StringUtils.EMPTY) String status,
                       @RequestParam(name = ConstantUtil.SORT_BY, required = false, defaultValue = StringUtils.EMPTY) String sortBy,
                       @RequestParam(name = ConstantUtil.SORT_DIR, required = false, defaultValue = StringUtils.EMPTY) String sortDir,
                       @RequestParam(name = ConstantUtil.PORTAL, required = false, defaultValue = StringUtils.EMPTY) String portal,
                       @RequestParam(name = ConstantUtil.SEARCH, required = false, defaultValue = StringUtils.EMPTY) String search) {
        Map<String, String> pagination = UtilHelper.getSearchMap(search, sortBy, sortDir, status, portal);
        UtilHelper.setTablePagination(articleInterface.findAll(UtilHelper.pageable(page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR)), pagination.get(ConstantUtil.PORTAL), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH)), page, size, pagination.get(ConstantUtil.SORT_BY), pagination.get(ConstantUtil.SORT_DIR), pagination.get(ConstantUtil.STATUS), pagination.get(ConstantUtil.SEARCH), "null", model);

        model.addAttribute(ConstantUtil.PORTAL, portal);
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.PORTAL_LIST, portalInterface.getActivePortal());
        model.addAttribute(ConstantUtil.PAGINATION_PREFIX, " /admin/article/list?page=");
        model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&status=" + status + "&portal=" + portal + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
        return PageConstants.ARTICLE_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String add(Model model, ArticleRequestDto articleRequestDto) {
        model.addAttribute(ConstantUtil.ARTICLE_DTO, articleRequestDto);
        getInitData(model);
        return PageConstants.ARTICLE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String add(Model model, @ModelAttribute(ConstantUtil.ARTICLE_DTO) @Valid ArticleRequestDto articleRequestDto,
                      BindingResult result, RedirectAttributes redirectAttributes) {
        if (validator(result, articleRequestDto, null)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            getInitData(model);
            return PageConstants.ARTICLE_ADD_PAGE;
        }
        if (articleInterface.save(articleRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Article added successfully");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Article add failed");
        return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
    }

    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(Model model,
                       @PathVariable(ConstantUtil.UKEY) String uKey,
                       RedirectAttributes redirectAttributes) {
        ArticleResponseDto articleResponseDto = articleInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || articleResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Article Id");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        model.addAttribute(ConstantUtil.ARTICLE_DTO, articleResponseDto);
        model.addAttribute(ConstantUtil.UKEY, uKey);
        getInitData(model);
        return PageConstants.ARTICLE_EDIT_PAGE;
    }


    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(Model model, @PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.ARTICLE_DTO) @Valid ArticleRequestDto articleRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes) {
        ArticleEntity articleEntity = articleInterface.findByKey(uKey);
        if (uKey == null || uKey.isEmpty() || articleEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Article Id");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            getInitData(model);
            return PageConstants.ARTICLE_EDIT_PAGE;
        }
        if (validator(result, articleRequestDto, articleEntity)) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.UKEY, uKey);
            getInitData(model);
            return PageConstants.ARTICLE_EDIT_PAGE;
        }

        if (articleInterface.update(articleRequestDto, articleEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Article updated successfully ");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR,  "Article not updated successfully ");
        return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
    }

    @GetMapping(UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.UKEY) String uKey, RedirectAttributes redirectAttributes) {
        //Repository side Validation
        ArticleResponseDto articleResponseDto = articleInterface.findByuKey(uKey);
        if (uKey == null || uKey.isEmpty() || articleResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Invalid Article Id");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        if (articleInterface.deleteByuKey(uKey)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS,  " Article deleted successfully");
            return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Article delete failed");
        return UrlConstants.REDIRECT_ARTICLE_LIST_URL;
    }

    private void getInitData(Model model) {
        model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        model.addAttribute(ConstantUtil.PORTAL_LIST, portalInterface.getActivePortal());
        model.addAttribute(ConstantUtil.CURRENT_DATE, LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        model.addAttribute(ConstantUtil.HOST_NAME,StringUtils.EMPTY);
    }

    private boolean validator(BindingResult result, ArticleRequestDto articleRequestDto, ArticleEntity articleEntity) {
        if (!UtilHelper.status().contains(articleRequestDto.getStatus())) {
            result.rejectValue(ConstantUtil.STATUS, ConstantUtil.INVALID_STATUS, ConstantUtil.ERROR_STATUS_INVALID_MSG);
        }
        if (articleEntity == null) {
            if (articleInterface.existsByPortalNameAndUrl(articleRequestDto.getPortalName(),articleRequestDto.getUrl())) {
                result.rejectValue(ConstantUtil.URL, ErrorUtil.URL_ERROR_CODE, ErrorUtil.ERROR_URL_EXIST);
            }
        } else {
            if (!(articleEntity.getUrl().equalsIgnoreCase(articleRequestDto.getUrl()) &&
                    articleEntity.getPortalName().equalsIgnoreCase(articleRequestDto.getPortalName())) && articleInterface.existsByPortalNameAndUrl(articleRequestDto.getPortalName(),articleRequestDto.getUrl())) {
                result.rejectValue(ConstantUtil.URL, ErrorUtil.URL_ERROR_CODE, ErrorUtil.ERROR_URL_EXIST);
            }
        }
        if (result.hasErrors()) {
            return true;
        }
        return false;
    }
}
