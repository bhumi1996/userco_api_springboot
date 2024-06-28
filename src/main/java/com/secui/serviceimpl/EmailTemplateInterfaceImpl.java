package com.secui.serviceimpl;

import com.secui.entity.EmailTemplateEntity;
import com.secui.repository.EmailTemplateRepository;
import com.secui.request.EmailTemplateRequestDto;
import com.secui.response.EmailTemplateResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.EmailTemplateInterface;
import com.secui.utility.FilterSearch;
import com.secui.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateInterfaceImpl implements EmailTemplateInterface {
    private final EmailTemplateRepository emailTemplateRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public boolean existsByTemplateName(String templateName) {
        return emailTemplateRepository.existsByTemplateName(templateName);
    }

    @Override
    public boolean save(EmailTemplateRequestDto emailTemplateRequestDto) {
        try {
            EmailTemplateEntity emailTemplateEntity = new EmailTemplateEntity();
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            emailTemplateEntity.setUKey(UtilHelper.uKey());
            emailTemplateEntity.setCreatedBy(userName);
            emailTemplateEntity.setLastModifiedBy(userName);
            mapping(emailTemplateEntity,emailTemplateRequestDto);
            emailTemplateRepository.save(emailTemplateEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailTemplateImpl :: Method Save() ::{} ", exception.getMessage());
            return false;
        }
    }



    @Override
    public EmailTemplateResponseDto findByuKey(String uKey) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findByuKey(uKey);
        if(emailTemplateEntity!=null){
            return modelMapper.map(emailTemplateEntity,EmailTemplateResponseDto.class);
        }
        return null;
    }

    @Override
    public EmailTemplateEntity findByKey(String uKey) {
        return emailTemplateRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(EmailTemplateRequestDto emailTemplateRequestDto, EmailTemplateEntity emailTemplateEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            mapping(emailTemplateEntity,emailTemplateRequestDto);
            emailTemplateEntity.setLastModifiedBy(userName);
            emailTemplateRepository.save(emailTemplateEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailTemplateImpl :: Method Update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            emailTemplateRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailTemplateImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public Page<EmailTemplateResponseDto> findAll(Pageable pageable, String template, String status, String search) {
        return modelMapper.map(emailTemplateRepository.findAll(filterSearch.getTemplateQuery(template,status,search),pageable),new TypeToken<Page<EmailTemplateResponseDto>>(){}.getType());
    }

    @Override
    public EmailTemplateResponseDto findByTemplateNameAndStatus(String leadMail, String active) {
        EmailTemplateEntity emailTemplateEntity = emailTemplateRepository.findByTemplateNameAndStatus(leadMail,active);
        if(emailTemplateEntity!=null){
            return modelMapper.map(emailTemplateEntity,EmailTemplateResponseDto.class);
        }
        return null;
    }

    private void mapping(EmailTemplateEntity emailTemplateEntity, EmailTemplateRequestDto emailTemplateRequestDto) {
        emailTemplateEntity.setTemplateName(emailTemplateRequestDto.getTemplateName());
        emailTemplateEntity.setTemplateHeading(emailTemplateRequestDto.getTemplateHeading());
        emailTemplateEntity.setTemplateBody(emailTemplateRequestDto.getTemplateBody());
        emailTemplateEntity.setStatus(emailTemplateRequestDto.getStatus());
    }
}
