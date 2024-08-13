package com.secui.mvc.service;

import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.request.UserRequestDto;
import com.secui.mvc.request.UserUpdateRequestDto;
import com.secui.mvc.response.UserResponseDto;
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
