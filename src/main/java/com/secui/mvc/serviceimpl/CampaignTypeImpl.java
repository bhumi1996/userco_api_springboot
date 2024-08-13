package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.CampaignTypeEntity;
import com.secui.mvc.repository.CampaignTypeRepository;
import com.secui.mvc.request.CampaignTypeRequestDto;
import com.secui.mvc.response.CampaignTypeResponseDto;
import com.secui.mvc.service.CampaignTypeInterface;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignTypeImpl implements CampaignTypeInterface {
    private final ModelMapper mapper;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;
    private final FilterSearch filterSearch;
    private final CampaignTypeRepository campaignTypeRepository;

    @Override
    public Page<CampaignTypeResponseDto> findAll(Pageable pageable, String status, String search) {
        return mapper.map(campaignTypeRepository.findAll(filterSearch.getTypeQuery(status, search), pageable), new TypeToken<Page<CampaignTypeResponseDto>>() {
        }.getType());
    }

    @Override
    public boolean save(CampaignTypeRequestDto campaignTypeRequestDto) {
        try {
            CampaignTypeEntity campaignTypeEntity = new CampaignTypeEntity();
            campaignTypeEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            campaignTypeEntity.setCreatedBy(userName);
            campaignTypeEntity.setLastModifiedBy(userName);
            mapper(campaignTypeEntity, campaignTypeRequestDto);
            campaignTypeRepository.save(campaignTypeEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CampaignTypeImpl :: Method save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapper(CampaignTypeEntity campaignTypeEntity, CampaignTypeRequestDto campaignTypeRequestDto) {
        campaignTypeEntity.setType(campaignTypeRequestDto.getType());
        campaignTypeEntity.setStatus(campaignTypeRequestDto.getStatus());
    }

    @Override
    public boolean existsByuKey(String uKey) {
        return campaignTypeRepository.existsByuKey(uKey);
    }

    @Override
    public CampaignTypeResponseDto findByuKey(String uKey) {
        CampaignTypeEntity campaignTypeEntity = campaignTypeRepository.findByuKey(uKey);
        if (campaignTypeEntity != null) {
            return mapper.map(campaignTypeEntity, CampaignTypeResponseDto.class);
        }
        return null;
    }

    @Override
    public CampaignTypeEntity findByKey(String uKey) {
        return campaignTypeRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(CampaignTypeRequestDto campaignTypeRequestDto, CampaignTypeEntity campaignTypeEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            campaignTypeEntity.setLastModifiedBy(userName);
            mapper(campaignTypeEntity, campaignTypeRequestDto);
            campaignTypeRepository.save(campaignTypeEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CampaignTypeImpl :: Method update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            campaignTypeRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CampaignTypeImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByType(String type) {
        return campaignTypeRepository.existsByType(type);
    }
}
