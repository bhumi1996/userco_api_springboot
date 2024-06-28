package com.secui.service;

import com.secui.entity.EmailConfigurationEntity;
import com.secui.request.EmailConfigurationRequestDto;
import com.secui.response.EmailConfigurationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailConfigurationInterface {
    Page<EmailConfigurationResponseDto> findAll(Pageable pageable, String status, String search);

    boolean save(EmailConfigurationRequestDto emailConfigurationRequestDto);

    EmailConfigurationResponseDto findByuKey(String uKey);

    EmailConfigurationEntity findByKey(String uKey);

    boolean update(EmailConfigurationRequestDto emailConfigurationRequestDto, EmailConfigurationEntity emailConfigurationEntity);

    boolean deleteByuKey(String uKey);

    boolean existsByName(String name);

    EmailConfigurationResponseDto findByStatus(String active);

    EmailConfigurationResponseDto findByName(String emailConfiguration);
}
