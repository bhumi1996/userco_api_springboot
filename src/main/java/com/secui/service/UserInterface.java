package com.secui.service;

import com.fasterxml.jackson.databind.BeanProperty;
import com.secui.entity.UserEntity;
import com.secui.request.UserRequestDto;
import com.secui.request.UserUpdateRequestDto;
import com.secui.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInterface {
    Page<UserResponseDto> findAll(Pageable pageable, String status, String search);

    boolean existsByEmail(String email);

    boolean save(UserRequestDto userRequestDto);

    void save(UserEntity userEntity);

    UserResponseDto findByuKey(String uKey);

    boolean update(UserUpdateRequestDto userUpdateRequestDto, String uKey);

    UserEntity findByKey(String uKey);

    boolean deleteByuKey(UserEntity userEntity);

    UserEntity findByEmail(String name);

    boolean update(UserEntity userModel);

    void resetFailedAttempts(String email);

    void increaseFailedAttempts(UserEntity userEntity);

    void lock(UserEntity userEntity);

    boolean unlockWhenTimeExpired(UserEntity userEntity);
}
