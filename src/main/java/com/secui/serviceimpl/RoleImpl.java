package com.secui.serviceimpl;

import com.secui.entity.RoleEntity;
import com.secui.repository.RoleRepository;
import com.secui.request.RoleRequestDto;
import com.secui.response.RoleResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.RoleInterface;
import com.secui.utility.ConstantUtil;
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
@RequiredArgsConstructor
@Slf4j
public class RoleImpl implements RoleInterface {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public Page<RoleResponseDto> findAll(Pageable pageable, String status, String search) {

        return modelMapper.map(roleRepository.findAll(filterSearch.getRoleQuery(status, search), pageable), new TypeToken<Page<RoleResponseDto>>() {
        }.getType());
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public boolean save(RoleRequestDto roleRequestDto) {
        try{
           RoleEntity roleEntity = new RoleEntity();
           roleEntity.setUKey(UtilHelper.uKey());
           String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
           roleEntity.setCreatedBy(userName);
           roleEntity.setLastModifiedBy(userName);
           mapping(roleEntity,roleRequestDto);
           roleRepository.save(roleEntity);
           return true;
        }catch (Exception exception){
            log.error("Exception in class RoleImpl :: Method save() :: {}",exception.getMessage());
             return false;
        }
    }

    private void mapping(RoleEntity roleEntity, RoleRequestDto roleRequestDto) {
       roleEntity.setName(roleRequestDto.getName());
       roleEntity.setDescription(roleRequestDto.getDescription());
       roleEntity.setStatus(roleRequestDto.getStatus());
    }

    @Override
    public RoleResponseDto findByuKey(String uKey) {
        RoleEntity roleEntity = roleRepository.findByuKey(uKey);
        if(roleEntity!=null){
            return modelMapper.map(roleEntity, RoleResponseDto.class);
        }
        return null;
    }

    @Override
    public RoleEntity findByKey(String uKey) {
        return roleRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(RoleRequestDto roleRequestDto, RoleEntity roleEntity) {
        try{
            mapping(roleEntity,roleRequestDto);
            roleEntity.setLastModifiedBy("Bhumika Dewanagnan");
            roleRepository.save(roleEntity);
            return true;
        }catch (Exception exception){
            log.error("Exception in RoleImpl :: Method update() ::{} ",exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            roleRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in RoleImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }

    }

    @Override
    public List<RoleResponseDto> findAll() {
        return modelMapper.map(roleRepository.findAllByStatus(ConstantUtil.ACTIVE),new TypeToken<List<RoleResponseDto>>(){}.getType());
    }

    @Override
    public boolean save(RoleEntity roleEntity) {
        try {
            roleRepository.save(roleEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in class RoleImpl :: Method save() {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name);
    }
}
