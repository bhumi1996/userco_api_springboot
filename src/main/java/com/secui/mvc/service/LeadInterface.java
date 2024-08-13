package com.secui.mvc.service;

import com.secui.mvc.entity.GroupEntity;
import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.request.GroupLeadsListDto;
import com.secui.mvc.request.LeadRequestDto;
import com.secui.mvc.response.LeadResponseDto;
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
