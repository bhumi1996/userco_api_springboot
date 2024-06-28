package com.secui.controller;

import com.secui.entity.LeadEntity;
import com.secui.request.LeadMailRequestDto;
import com.secui.response.EmailConfigurationResponseDto;
import com.secui.response.EmailTemplateResponseDto;
import com.secui.service.*;
import com.secui.utility.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = UrlConstants.LEAD_BASE_URL + UrlConstants.LEAD_MAIL)
@Slf4j
public class LeadMailController implements InitBinderInterface {

    private final LeadMailInterface leadMailInterface;

    private final LeadInterface leadInterface;

    private final EmailTemplateInterface emailTemplateInterface;

    private final EmailConfigurationInterface emailConfigurationInterface;

    private final MailApiInterface mailApiInterface;

    private final CommonPropertiesUtil commonPropertiesUtil;

    @GetMapping
    public String list(Model model, @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                       RedirectAttributes redirectAttributes) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        model.addAttribute(ConstantUtil.LIST, leadMailInterface.finaAllByLead(leadEntity));
        model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
        model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
        model.addAttribute(ConstantUtil.LEAD_INFO, leadEntity);
        return PageConstants.LEAD_MAIL_LIST_PAGE;
    }

    @GetMapping(UrlConstants.ADD_URL)
    public String sendLeadMail(Model model, @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                               @ModelAttribute(ConstantUtil.LEAD_MAIL_DTO) @Valid LeadMailRequestDto leadMailRequestDto,
                               RedirectAttributes redirectAttributes) {

        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry,Invalid lead information");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        EmailConfigurationResponseDto emailConfigurationResponseDto = emailConfigurationInterface.findByStatus(ConstantUtil.ACTIVE);
        if (emailConfigurationResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Email configuration with either not exist or blocked please contact to admin");
            return UrlConstants.REDIRECT_LEAD_MAIL_URL;
        }
        EmailTemplateResponseDto emailTemplateResponseDtos = emailTemplateInterface.findByTemplateNameAndStatus(ConstantUtil.LEAD_MAIL, ConstantUtil.ACTIVE);
        if (emailTemplateResponseDtos == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Email Template with either not exist or blocked please contact to admin");
            return UrlConstants.REDIRECT_LEAD_MAIL_URL;
        }
        ;
        //set email to lead mail dto
        leadMailRequestDto.setEmail(UtilHelper.mask(leadEntity.getEmail()));
        leadMailRequestDto.setName(leadEntity.getLeadName());
        model.addAllAttributes(getMailDataLists(leadEntity, leaduKey));
        model.addAttribute(ConstantUtil.LEAD_MAIL_DTO, leadMailRequestDto);
        return PageConstants.LEAD_MAIl_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String sendLeadMail(Model model,
                               @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                               @ModelAttribute(ConstantUtil.LEAD_MAIL_DTO) @Valid LeadMailRequestDto leadMailRequestDto,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty()) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry,Invalid lead information");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAllAttributes(getMailDataLists(leadEntity, leaduKey));
            return PageConstants.LEAD_MAIl_ADD_PAGE;
        }
        leadMailRequestDto.setEmail(leadEntity.getEmail());
        mailApiInterface.sendLeadMail(leadMailRequestDto, leadEntity);
        if (leadMailInterface.save(leadMailRequestDto, leadEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Mail Send Successfully");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Mail failed");
        return UrlConstants.REDIRECT_LEAD_LIST_URL;
    }

    private Map<String, Object> getMailDataLists(LeadEntity leadEntity, String leaduKey) {
        Map<String, Object> model = new HashMap<>();
        model.put(ConstantUtil.LEAD_UKEY, leaduKey);
        model.put(ConstantUtil.NAME, leadEntity.getLeadName());
        model.put(ConstantUtil.LEAD_INFO, leadEntity);
        return model;
    }


}
