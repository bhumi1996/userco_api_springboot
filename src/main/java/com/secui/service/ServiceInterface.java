package com.secui.service;

import com.secui.entity.ServiceEntity;
import com.secui.request.ServiceRequestDto;
import com.secui.response.ServiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceInterface {
    Page<ServiceResponseDto> findAll(Pageable pageable, String status, String search);

    boolean save(ServiceRequestDto serviceRequestDto);

    boolean existsByuKey(String uKey);

    ServiceResponseDto findByuKey(String uKey);

    ServiceEntity findByKey(String uKey);

    boolean update(ServiceRequestDto serviceRequestDto, ServiceEntity serviceEntity);

    boolean deleteByuKey(String uKey);

    boolean existsByServiceName(String serviceName);
}
