package com.secui.mvc.service;

import com.secui.mvc.entity.ServiceEntity;
import com.secui.mvc.request.ServiceRequestDto;
import com.secui.mvc.response.ServiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ServiceInterface {
    Page<ServiceResponseDto> findAll(Pageable pageable, String status, String search);
    ResponseEntity<Map<String,Object>> findAll(Pageable pageable);

    boolean save(ServiceRequestDto serviceRequestDto);

    boolean existsByuKey(String uKey);

    ServiceResponseDto findByuKey(String uKey);

    ServiceEntity findByKey(String uKey);

    boolean update(ServiceRequestDto serviceRequestDto, ServiceEntity serviceEntity);

    boolean deleteByuKey(String uKey);

    boolean existsByServiceName(String serviceName);
}
