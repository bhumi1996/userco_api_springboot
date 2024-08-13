package com.secui.mvc.service;

import com.secui.mvc.entity.EmailTemplateEntity;
import com.secui.mvc.request.EmailTemplateRequestDto;
import com.secui.mvc.response.EmailTemplateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailTemplateInterface {
    boolean existsByTemplateName(String templateName);

    boolean save(EmailTemplateRequestDto emailTemplateRequestDto);

    EmailTemplateResponseDto findByuKey(String uKey);

    EmailTemplateEntity findByKey(String uKey);

    boolean update(EmailTemplateRequestDto emailTemplateRequestDto, EmailTemplateEntity emailTemplateEntity);

    boolean deleteByuKey(String uKey);

    Page<EmailTemplateResponseDto> findAll(Pageable pageable, String template, String status, String search);

    EmailTemplateResponseDto findByTemplateNameAndStatus(String leadMail, String active);

}
