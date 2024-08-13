package com.secui.mvc.service;

import com.secui.mvc.entity.CampaignTypeEntity;
import com.secui.mvc.request.CampaignTypeRequestDto;
import com.secui.mvc.response.CampaignTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CampaignTypeInterface {
    Page<CampaignTypeResponseDto> findAll(Pageable pageable, String status, String search);

    boolean save(CampaignTypeRequestDto campaignTypeRequestDto);

    boolean existsByuKey(String uKey);

    CampaignTypeResponseDto findByuKey(String uKey);

    CampaignTypeEntity findByKey(String uKey);

    boolean update(CampaignTypeRequestDto campaignTypeRequestDto, CampaignTypeEntity campaignTypeEntity);

    boolean deleteByuKey(String uKey);

    boolean existsByType(String type);
}
