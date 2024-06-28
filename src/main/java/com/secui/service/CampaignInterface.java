package com.secui.service;

import com.secui.entity.CampaignEntity;
import com.secui.entity.GroupEntity;
import com.secui.request.CampaignRequestDto;
import com.secui.request.GroupListDto;
import com.secui.response.CampaignResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignInterface {
    boolean existByName(String name);

    CampaignResponseDto findByuKey(String uKey);

    CampaignEntity findByKey(String uKey);

    boolean update(CampaignRequestDto campaignRequestDto, CampaignEntity campaignEntity);

    boolean deleteByuKey(String uKey);

    Page<CampaignResponseDto> findAll(Pageable pageable, String type, String status, String search);

    boolean save(CampaignRequestDto campaignRequestDto);

    List<GroupEntity> findAllByStatus(String uKey, String active);

    boolean addGroupToCampaign(GroupListDto groupListDto, CampaignEntity campaignEntity);

    boolean removeGroupFromCampaign(GroupListDto groupListDto, CampaignEntity campaignEntity);
}
