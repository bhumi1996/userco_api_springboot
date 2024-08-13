package com.secui.mvc.service;

import com.secui.mvc.request.PortalRequestDto;
import com.secui.mvc.response.PortalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortalInterface {
    
    
    boolean save(PortalRequestDto portalRequestDto);

    PortalResponseDto findByuKey(String uKey);

    boolean update(PortalRequestDto portalRequestDto, String uKey);

    boolean deleteByuKey(String uKey);

    boolean existsByPortalName(String portalName);

    boolean existsByShortName(String shortName);

    boolean existsByDomainName(String domainName);

    Page<PortalResponseDto> findAll(Pageable pageable, String status, String search);

    List<String> getActivePortal();
}
