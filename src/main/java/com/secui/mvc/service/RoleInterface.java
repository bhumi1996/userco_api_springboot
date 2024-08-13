package com.secui.mvc.service;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.request.RoleRequestDto;
import com.secui.mvc.response.RoleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleInterface {
    Page<RoleResponseDto> findAll(Pageable pageable, String status, String search);

    boolean existsByName(String name);

    boolean save(RoleRequestDto roleRequestDto);

    RoleResponseDto findByuKey(String uKey);

    RoleEntity findByKey(String uKey);

    boolean update(RoleRequestDto roleRequestDto, RoleEntity roleEntity);

    boolean deleteByuKey(String uKey);

    List<RoleResponseDto>findAll();
    boolean save(RoleEntity roleEntity);

    RoleEntity findByName(String name);
}
