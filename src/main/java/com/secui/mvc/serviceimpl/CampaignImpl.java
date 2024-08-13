package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.CampaignEntity;
import com.secui.mvc.entity.GroupEntity;
import com.secui.mvc.repository.CampaignRepository;
import com.secui.mvc.request.CampaignRequestDto;
import com.secui.mvc.request.GroupListDto;
import com.secui.mvc.response.CampaignResponseDto;
import com.secui.mvc.service.CampaignInterface;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.GroupInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignImpl implements CampaignInterface {
    private final CampaignRepository campaignRepository;
    private final GroupInterface groupInterface;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public boolean existByName(String name) {
        return campaignRepository.existsByName(name);
    }

    @Override
    public CampaignResponseDto findByuKey(String uKey) {
        CampaignEntity campaignEntity = campaignRepository.findByuKey(uKey);
        if (campaignEntity != null) {
            return modelMapper.map(campaignEntity, CampaignResponseDto.class);
        }
        return null;
    }

    @Override
    public CampaignEntity findByKey(String uKey) {
        return campaignRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(CampaignRequestDto campaignRequestDto, CampaignEntity campaignEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            campaignEntity.setLastModifiedBy(userName);
            mapper(campaignEntity, campaignRequestDto);
            campaignRepository.save(campaignEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in CampaignImpl :: Method update() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            campaignRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception ex) {
            log.error("Exception in CampaignImpl :: Method deleteByuKey() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public Page<CampaignResponseDto> findAll(Pageable pageable, String type, String status, String search) {
        return modelMapper.map(campaignRepository.findAll(filterSearch.getCampaignQuery(type,status,search),pageable),new TypeToken<Page<CampaignResponseDto>>(){}.getType());
    }

    @Override
    public boolean save(CampaignRequestDto campaignRequestDto) {
        try {
            CampaignEntity campaignEntity = new CampaignEntity();
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            campaignEntity.setUKey(UtilHelper.uKey());
            campaignEntity.setCreatedBy(userName);
            campaignEntity.setLastModifiedBy(userName);
            mapper(campaignEntity, campaignRequestDto);
            campaignRepository.save(campaignEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in CampaignImpl :: Method save() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<GroupEntity> findAllByStatus(String uKey, String active) {
        CampaignEntity campaignEntity = campaignRepository.findByuKey(uKey);
        List<GroupEntity> groupModels = new ArrayList<>();
        if (campaignEntity != null) {
            Set<GroupEntity> groupEntitySet = campaignEntity.getGroups();
            List<GroupEntity> groupEntityList = groupInterface.findAllByStatus(active);
            for (GroupEntity groupEntity : groupEntityList) {
                if (!groupEntitySet.contains(groupEntity)) {
                    groupModels.add(groupEntity);
                }
            }
        }
        return groupModels;
    }

    @Override
    public boolean addGroupToCampaign(GroupListDto groupListDto, CampaignEntity campaignEntity) {
        try {
            Set<GroupEntity> groupEntitySet = new HashSet<>();
            if (groupListDto != null && !groupListDto.getGroupList().isEmpty()) {
                groupEntitySet = groupInterface.findAllByuKey(groupListDto.getGroupList()).stream().collect(Collectors.toSet());
            }
            if (campaignEntity.getGroups() != null && !campaignEntity.getGroups().isEmpty()) {
                campaignEntity.getGroups().addAll(groupEntitySet);
            } else {
                campaignEntity.setGroups(groupEntitySet);
            }
            campaignRepository.save(campaignEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception CampaignImpl::addGroupToCampaign() Method::{}",ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeGroupFromCampaign(GroupListDto groupListDto, CampaignEntity campaignEntity) {
        try {
            Set<GroupEntity> groups = new HashSet<>();
            if (groupListDto != null && !groupListDto.getGroupList().isEmpty()) {
                groups = groupInterface.findAllByuKey(groupListDto.getGroupList()).stream().collect(Collectors.toSet());
            }
            for (GroupEntity group : groups) {
                group.getCampaigns().remove(campaignEntity);
            }
            campaignEntity.getGroups().removeAll(groups);
            campaignRepository.save(campaignEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception CampaignImpl::removeGroupFromCampaign() Method::{}", ex.getMessage());
            return false;
        }
    }

    private void mapper(CampaignEntity campaignEntity, CampaignRequestDto campaignRequestDto) {
        campaignEntity.setName(campaignRequestDto.getName());
        campaignEntity.setStatus(campaignRequestDto.getStatus());
    }
}
