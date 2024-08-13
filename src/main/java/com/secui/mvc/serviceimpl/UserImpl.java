package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.repository.UserRepository;
import com.secui.mvc.request.UserRequestDto;
import com.secui.mvc.request.UserUpdateRequestDto;
import com.secui.mvc.response.UserResponseDto;
import com.secui.mvc.service.UserInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl implements UserInterface {

    private final UserRepository userRepository;
    private final FilterSearch filterSearch;
    private final ModelMapper mapper;
    @Value("${LOCK_TIME_DURATION}")
    private Long lockDuration;

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable, String status, String search) {
        return mapper.map(userRepository.findAll(filterSearch.getUserQuery(status, search), pageable), new TypeToken<Page<UserResponseDto>>() {
        }.getType());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean save(UserRequestDto userRequestDto) {
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUKey(UtilHelper.uKey());
            userEntity.setCreatedBy(userRequestDto.getCreatedBy());
            userEntity.setLastModifiedBy(userRequestDto.getLastModifiedBy());
            mapping(userEntity, userRequestDto);
            Set<UserEntity> users = new LinkedHashSet<>();
            users.add(userEntity);
            for (RoleEntity role : userRequestDto.getRoles()) {
                role.setUsers(users);
            }
            userEntity.setRoles(userRequestDto.getRoles());
            userRepository.save(userEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in class UserImpl:: Method save() ::ERROR::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    private void mapping(UserEntity userEntity, UserRequestDto userRequestDto) {
        userEntity.setFullName(userRequestDto.getFullName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setMobileNo(userRequestDto.getMobileNo());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userRequestDto.getPassword()));
        userEntity.setEnabled(true);
    }

    @Override
    public UserResponseDto findByuKey(String uKey) {
        UserEntity userEntity = userRepository.findByuKey(uKey);
        if(userEntity!=null){
            return mapper.map(userEntity,UserResponseDto.class);
        }
        return null;
    }

    @Override
    public boolean update(UserUpdateRequestDto userUpdateRequestDto, String uKey) {
        try {
            UserEntity userEntity = userRepository.findByuKey(uKey);
            userEntity.setFullName(userUpdateRequestDto.getFullName());
            userEntity.setMobileNo(userUpdateRequestDto.getMobileNo());
            userEntity.setEmail(userUpdateRequestDto.getEmail());
            userEntity.getRoles().clear();
            for (RoleEntity role : userUpdateRequestDto.getRoles()) {
                userEntity.getRoles().add(role);
            }
            userEntity.setLastModifiedBy(userUpdateRequestDto.getLastModifiedBy());
            userRepository.save(userEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in class UserImpl:: Method is update() ::ERROR::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public UserEntity findByKey(String uKey) {
        return userRepository.findByuKey(uKey);
    }

    @Override
    public boolean deleteByuKey(UserEntity userEntity) {
        try {

            userEntity.getRoles().clear();
            userRepository.deleteByuKey(userEntity.getUKey());
            return true;
        } catch (Exception ex) {
            log.error("Exception in class UserImpl :: Method deleteByuKey() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public UserEntity findByEmail(String name) {
        return userRepository.findByEmail(name);
    }

    @Override
    public boolean update(UserEntity userModel) {
        try {
            userRepository.save(userModel);
            return true;
        } catch (Exception ex) {
            log.error("Exception in class UserImpl:: Method update() ::ERROR::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    @Override
    public void increaseFailedAttempts(UserEntity userEntity) {
        int newFailAttempts = userEntity.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, userEntity.getEmail());
    }

    @Override
    public void lock(UserEntity userEntity) {
        userEntity.setAccountLocked(false);
        userEntity.setLockTime(new Date());
        userRepository.save(userEntity);
    }

    @Override
    public boolean unlockWhenTimeExpired(UserEntity userEntity) {
        long lockTimeInMillis = userEntity.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + lockDuration < currentTimeInMillis) {
            userEntity.setAccountLocked(true);
            userEntity.setLockTime(null);
            userEntity.setFailedAttempt(0);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}
