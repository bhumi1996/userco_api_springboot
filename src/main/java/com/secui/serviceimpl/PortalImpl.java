package com.secui.serviceimpl;

import com.secui.entity.PortalEntity;
import com.secui.repository.PortalRepository;
import com.secui.request.PortalRequestDto;
import com.secui.response.PortalResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.PortalInterface;
import com.secui.utility.FilterSearch;
import com.secui.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortalImpl implements PortalInterface {
    private final PortalRepository portalRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public boolean save(PortalRequestDto portalRequestDto) {
        try {
           PortalEntity portalEntity = new PortalEntity();
           portalEntity.setUKey(UtilHelper.uKey());
           String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
           portalEntity.setLastModifiedBy(userName);
           portalEntity.setCreatedBy(userName);
           mapping(portalEntity,portalRequestDto);
            portalRepository.save(portalEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in PortalImpl :: Method save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapping(PortalEntity portalEntity, PortalRequestDto portalRequestDto) {
        portalEntity.setPortalName(portalRequestDto.getPortalName());
        portalEntity.setShortName(portalRequestDto.getShortName().toUpperCase());
        portalEntity.setDomainName(portalRequestDto.getDomainName());
        portalEntity.setCopyright(portalRequestDto.getCopyright());
        portalEntity.setDisclaimer(portalRequestDto.getDisclaimer());
        portalEntity.setStatus(portalRequestDto.getStatus());
        portalEntity.setSupportEmail(portalRequestDto.getSupportEmail());
        portalEntity.setWhatsAppNo(portalRequestDto.getWhatsAppNo());
        portalEntity.setAddress(portalRequestDto.getAddress());
        portalEntity.setFacebookUrl(portalRequestDto.getFacebookUrl());
        portalEntity.setTwitterUrl(portalRequestDto.getTwitterUrl());
        portalEntity.setLinkedinUrl(portalRequestDto.getLinkedinUrl());
        portalEntity.setYoutubeUrl(portalRequestDto.getYoutubeUrl());
        portalEntity.setInstagramUrl(portalRequestDto.getInstagramUrl());
        portalEntity.setTrustPilotUrl(portalRequestDto.getTrustPilotUrl());
    }

    @Override
    public PortalResponseDto findByuKey(String uKey) {
        PortalEntity portalEntity = portalRepository.findByuKey(uKey);
        if(portalEntity!=null){
            return modelMapper.map(portalEntity,PortalResponseDto.class);
        }
        return null;
    }

    @Override
    public boolean update(PortalRequestDto portalRequestDto, String uKey) {
        try {
            PortalEntity portalEntity = portalRepository.findByuKey(uKey);
            portalEntity.setLastModifiedBy(currentUserAuthenticationServiceInterface.getCurrentUserName());
            mapping(portalEntity,portalRequestDto);
            portalRepository.save(portalEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in PortalImpl :: Method update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            portalRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in PortalImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }

    }

    @Override
    public boolean existsByPortalName(String portalName) {
        return portalRepository.existsByPortalName(portalName);
    }

    @Override
    public boolean existsByShortName(String shortName) {
        return portalRepository.existsByShortName(shortName);
    }

    @Override
    public boolean existsByDomainName(String domainName) {
        return portalRepository.existsByDomainName(domainName);
    }

    @Override
    public Page<PortalResponseDto> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(portalRepository.findAll(filterSearch.getPortalQuery(status,search),pageable),new TypeToken<Page<PortalResponseDto>>(){}.getType());
    }
}
