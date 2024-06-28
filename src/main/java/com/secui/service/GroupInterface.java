package com.secui.service;

import com.secui.entity.GroupEntity;
import com.secui.request.GroupRequestDto;
import com.secui.response.GroupResponseDto;
import org.codehaus.groovy.runtime.StreamGroovyMethods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupInterface  {
    Page<GroupResponseDto> findAll(Pageable pageable, String status, String search);

    boolean existsByGroupName(String groupName);

    GroupResponseDto findByuKey(String uKey);

    GroupEntity findByKey(String uKey);

    boolean update(GroupRequestDto groupRequestDto, GroupEntity groupEntity);

    boolean deleteByuKey(String uKey);

    boolean save(GroupRequestDto groupRequestDto);

    GroupEntity saveAll(GroupEntity groupEntity);

    List<GroupEntity> findAllByStatus(String active);

    List<GroupEntity> findAllByuKey(List<String> groupList);
}
