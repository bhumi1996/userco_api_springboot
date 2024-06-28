package com.secui.serviceimpl;

import com.secui.entity.EmailConfigurationEntity;
import com.secui.repository.EmailConfigurationRepository;
import com.secui.request.EmailConfigurationRequestDto;
import com.secui.response.EmailConfigurationResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.EmailConfigurationInterface;
import com.secui.utility.FilterSearch;
import com.secui.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConfigurationImpl implements EmailConfigurationInterface {
    private final EmailConfigurationRepository emailConfigurationRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;


    @Override
    public Page<EmailConfigurationResponseDto> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(emailConfigurationRepository.findAll(filterSearch.getConfigurationQuery(status,search),pageable), new TypeToken<Page<EmailConfigurationResponseDto>>(){}.getType());
    }

    @Override
    public boolean save(EmailConfigurationRequestDto emailConfigurationRequestDto) {
        try {
            EmailConfigurationEntity emailConfigurationEntity = new EmailConfigurationEntity();
            emailConfigurationEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            emailConfigurationEntity.setLastModifiedBy(userName);
            emailConfigurationEntity.setCreatedBy(userName);
            mapper(emailConfigurationEntity,emailConfigurationRequestDto);
            emailConfigurationRepository.save(emailConfigurationEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailConfigurationImpl :: Method Save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapper(EmailConfigurationEntity emailConfigurationEntity, EmailConfigurationRequestDto emailConfigurationRequestDto) {
     emailConfigurationEntity.setName(emailConfigurationRequestDto.getName());
     emailConfigurationEntity.setEncoding(emailConfigurationRequestDto.getEncoding());
     emailConfigurationEntity.setPort(emailConfigurationRequestDto.getPort());
     emailConfigurationEntity.setProtocol(emailConfigurationRequestDto.getProtocol());
     emailConfigurationEntity.setTestConnection(emailConfigurationRequestDto.getTestConnection());
     emailConfigurationEntity.setSmtpAuth(emailConfigurationRequestDto.getSmtpAuth());
     emailConfigurationEntity.setSmtpAuth(emailConfigurationRequestDto.getSmtpAuth());
     emailConfigurationEntity.setStartTlsEnable(emailConfigurationRequestDto.getStartTlsEnable());
     emailConfigurationEntity.setFromMail(emailConfigurationRequestDto.getFromMail());
     emailConfigurationEntity.setReplyTo(emailConfigurationRequestDto.getReplyTo());
     emailConfigurationEntity.setCc(emailConfigurationRequestDto.getCc());
     emailConfigurationEntity.setBcc(emailConfigurationRequestDto.getBcc());
     emailConfigurationEntity.setUserName(emailConfigurationRequestDto.getUserName());
     emailConfigurationEntity.setPassword(emailConfigurationRequestDto.getPassword());
     emailConfigurationEntity.setHostName(emailConfigurationRequestDto.getHostName());
     emailConfigurationEntity.setStatus(emailConfigurationRequestDto.getStatus());

    }

    @Override
    public EmailConfigurationResponseDto findByuKey(String uKey) {
        EmailConfigurationEntity emailConfigurationEntity = emailConfigurationRepository.findByuKey(uKey);
        if(emailConfigurationEntity!=null){
            return modelMapper.map(emailConfigurationEntity,EmailConfigurationResponseDto.class);
        }
        return null;
    }

    @Override
    public EmailConfigurationEntity findByKey(String uKey) {
        return emailConfigurationRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(EmailConfigurationRequestDto emailConfigurationRequestDto, EmailConfigurationEntity emailConfigurationEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            emailConfigurationEntity.setLastModifiedBy(userName);
            mapper(emailConfigurationEntity,emailConfigurationRequestDto);
            emailConfigurationRepository.save(emailConfigurationEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailConfigurationImpl :: Method Update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            emailConfigurationRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in EmailConfigurationImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByName(String name) {
        return emailConfigurationRepository.existsByName(name);
    }

    @Override
    public EmailConfigurationResponseDto findByStatus(String active) {
        return emailConfigurationRepository.findByStatus(active);
    }

    @Override
    public EmailConfigurationResponseDto findByName(String emailConfiguration) {
        EmailConfigurationEntity emailConfigurationEntity = emailConfigurationRepository.findByName(emailConfiguration);
        if(emailConfigurationEntity!=null){
            return modelMapper.map(emailConfigurationEntity,EmailConfigurationResponseDto.class);
        }
        return null;
    }
}
