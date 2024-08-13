package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.Auditable;
import com.secui.mvc.entity.GroupEntity;
import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.repository.LeadRepository;
import com.secui.mvc.request.GroupLeadsListDto;
import com.secui.mvc.request.LeadRequestDto;
import com.secui.mvc.response.LeadResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.GroupInterface;
import com.secui.mvc.service.LeadInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadImpl implements LeadInterface {
    private final LeadRepository leadRepository;
    private final GroupInterface groupInterface;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public boolean existsByEmail(String email) {
        return leadRepository.existsByEmail(email);
    }

    @Override
    public boolean save(LeadRequestDto leadRequestDto) {
        try {
           LeadEntity leadEntity = new LeadEntity();
           String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
           leadEntity.setUKey(UtilHelper.uKey());
           leadEntity.setCreatedBy(userName);
           leadEntity.setLastModifiedBy(userName);
           mapper(leadEntity,leadRequestDto);
           leadRepository.save(leadEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in LeadImpl :: Method Save() ::{} ", exception.getMessage());
            return false;
        }
    }



    @Override
    public LeadResponseDto findByuKey(String uKey) {
        LeadEntity leadEntity = leadRepository.findByuKey(uKey);
        if(leadEntity!=null){
            return modelMapper.map(leadEntity,LeadResponseDto.class);
        }
        return null;
    }

    @Override
    public boolean update(LeadRequestDto leadRequestDto, LeadEntity leadEntity) {
        try {
           mapper(leadEntity,leadRequestDto);
           leadEntity.setLastModifiedBy(currentUserAuthenticationServiceInterface.getCurrentUserName());
           leadRepository.save(leadEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in LeadImpl :: Method Update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public LeadEntity findByKey(String uKey) {
        return leadRepository.findByuKey(uKey);
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            leadRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in LeadImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public Page<LeadResponseDto> findAll(Pageable pageable, String stage, String channel, String search) {
        return modelMapper.map(leadRepository.findAll(filterSearch.getLeadQuery(stage,channel,search),pageable),new TypeToken<Page<LeadResponseDto>>(){}.getType());
    }

    @Override
    public Page<LeadResponseDto> findAllPortalLeads(String uKey, Pageable pageable, String search) {
        GroupEntity groupEntity = groupInterface.findByKey(uKey);
        List<Long> pIds = new ArrayList<>();
        //Get Already assigned subscribers list for group
        if (groupEntity != null && !groupEntity.getLeads().isEmpty()) {
            pIds.addAll(groupEntity.getLeads().stream().map(Auditable::getPId).toList());
        }
        return modelMapper.map(leadRepository.findAll(filterSearch.addGroupLeadQuery(pIds, search), pageable),new TypeToken<Page<LeadResponseDto>>(){}.getType());
    }

    @Override
    public boolean addLeadsToGroup(GroupLeadsListDto groupLeadsListDto, String uKey) {
        try {
            if (groupLeadsListDto != null && !groupLeadsListDto.getLeadDtoList().isEmpty()) {
                Set<LeadResponseDto> leadSet = new HashSet<>(groupLeadsListDto.getLeadDtoList());
                GroupEntity groupEntity = groupInterface.findByKey(uKey);
                Set<LeadEntity> leadEntitySet = new HashSet<>();
                for (LeadResponseDto leadResponseDto : leadSet) {
                    if (leadResponseDto.getUKey() != null) {
                        LeadEntity leadEntity = leadRepository.findByuKey(leadResponseDto.getUKey());
                        leadEntity.getGroups().add(groupEntity);
                        leadRepository.save(leadEntity);
                        leadEntitySet.add(leadEntity);
                    }
                }
                groupEntity.getLeads().addAll(leadEntitySet);
                if (groupInterface.saveAll(groupEntity) != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Map<String, Object> findAllGroupLeads(GroupEntity groupEntity, Pageable pageSort, String search) {
        Map<String, Object> pages = new HashMap<>();
        List<Long> pIds = new ArrayList<>();
        //Get All group subscribers
        if (!groupEntity.getLeads().isEmpty()) {
            pIds.addAll(groupEntity.getLeads().stream().map(Auditable::getPId).toList());
        }
        //Filter and pagination based on search
        Page<LeadEntity> leadEntityPage = leadRepository.findAll(filterSearch.groupLeadFilterQuery(pIds, search), pageSort);
        pages.put(ConstantUtil.LIST,leadEntityPage.getContent());
        pages.put(ConstantUtil.TOTAL_PAGES, leadEntityPage.getTotalPages());
        pages.put(ConstantUtil.TOTAL_ELEMENTS, leadEntityPage.getTotalElements());
        return pages;
    }

    @Override
    public boolean removeFromGroup(String uKey, String leaduKey) {
        boolean result = false;
        GroupEntity groupEntity = groupInterface.findByKey(uKey);
        Set<LeadEntity> leads = groupEntity.getLeads();
        LeadEntity leadEntity = leadRepository.findByuKey(leaduKey);
        if (leads.contains(leadEntity)) {
            if (leadEntity.getGroups() != null && !leadEntity.getGroups().isEmpty()) {
                leadEntity.getGroups().remove(groupEntity);
            }
            leads.remove(leadEntity);
            leadRepository.save(leadEntity);
            result = true;
        }
        return result;
    }

    private void mapper(LeadEntity leadEntity, LeadRequestDto leadRequestDto) {
        leadEntity.setPortalName(leadRequestDto.getPortalName());
        leadEntity.setLeadName(leadRequestDto.getLeadName());
        leadEntity.setEmail(leadRequestDto.getEmail());
        leadEntity.setCountry(leadRequestDto.getCountry());
        leadEntity.setStage(leadRequestDto.getStage());
        leadEntity.setChannel(leadRequestDto.getChannel());
        leadEntity.setGender(leadRequestDto.getGender());
        leadEntity.setDialCode(leadRequestDto.getDialCode());
        leadEntity.setMobileNumber(leadRequestDto.getMobileNumber());

    }
}
