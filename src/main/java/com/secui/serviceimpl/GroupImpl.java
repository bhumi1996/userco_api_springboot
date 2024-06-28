package com.secui.serviceimpl;

import com.secui.entity.GroupEntity;
import com.secui.repository.GroupRepository;
import com.secui.request.GroupRequestDto;
import com.secui.response.GroupResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.GroupInterface;
import com.secui.utility.FilterSearch;
import com.secui.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupImpl implements GroupInterface {
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public Page<GroupResponseDto> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(groupRepository.findAll(filterSearch.getGroupQuery(status,search),pageable),new TypeToken<Page<GroupResponseDto>>(){}.getType());
    }

    @Override
    public boolean existsByGroupName(String groupName) {
        return groupRepository.existsByGroupName(groupName);
    }

    @Override
    public GroupResponseDto findByuKey(String uKey) {
        GroupEntity groupEntity = groupRepository.findByuKey(uKey);
        if(groupEntity!=null){
            return modelMapper.map(groupEntity,GroupResponseDto.class);
        }
        return null;
    }

    @Override
    public GroupEntity findByKey(String uKey) {
        return groupRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(GroupRequestDto groupRequestDto, GroupEntity groupEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            groupEntity.setLastModifiedBy(userName);
            mapper(groupEntity,groupRequestDto);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in GroupImpl :: Method update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            groupRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in GroupImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(GroupRequestDto groupRequestDto) {
        try {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            groupEntity.setCreatedBy(userName);
            groupEntity.setLastModifiedBy(userName);
            mapper(groupEntity,groupRequestDto);
            groupRepository.save(groupEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in GroupImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public GroupEntity saveAll(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    @Override
    public List<GroupEntity> findAllByStatus(String active) {
        return groupRepository.findAllByStatus(active);
    }

    @Override
    public List<GroupEntity> findAllByuKey(List<String> uKeys) {
        return groupRepository.findAllByuKey(uKeys);
    }

    private void mapper(GroupEntity groupEntity, GroupRequestDto groupRequestDto) {
    groupEntity.setGroupName(groupRequestDto.getGroupName());
    groupEntity.setStatus(groupRequestDto.getStatus());
    }
}
