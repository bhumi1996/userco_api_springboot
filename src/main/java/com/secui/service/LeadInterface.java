package com.secui.service;

import com.secui.entity.GroupEntity;
import com.secui.entity.LeadEntity;
import com.secui.request.GroupLeadsListDto;
import com.secui.request.LeadRequestDto;
import com.secui.response.LeadResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface LeadInterface {
    boolean existsByEmail(String email);

    boolean save(LeadRequestDto leadRequestDto);

    LeadResponseDto findByuKey(String uKey);

    boolean update(LeadRequestDto leadRequestDto, LeadEntity leadEntity);

    LeadEntity findByKey(String uKey);

    boolean deleteByuKey(String uKey);

    Page<LeadResponseDto> findAll(Pageable pageable, String stage, String channel, String search);

    Page<LeadResponseDto> findAllPortalLeads(String uKey, Pageable pageable, String search);

    boolean addLeadsToGroup(GroupLeadsListDto groupLeadsListDto, String uKey);

    Map<String, Object> findAllGroupLeads(GroupEntity groupEntity, Pageable pageSort, String search);

    boolean removeFromGroup(String uKey, String leaduKey);
}
