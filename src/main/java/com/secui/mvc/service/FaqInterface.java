package com.secui.mvc.service;

import com.secui.mvc.entity.FaqEntity;
import com.secui.mvc.request.FaqRequestDto;
import com.secui.mvc.response.FaqResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface FaqInterface {
    Page<FaqEntity> findAll(Pageable pageable, String status, String search);

    boolean save(FaqRequestDto faqRequestDto);

    boolean existsByFaqQuestion(String faqQuestion);

    boolean existsByuKey(String uKey);

    FaqResponseDto findByuKey(String uKey);

    FaqEntity findByKey(String uKey);

    boolean update(FaqRequestDto faqRequestDto, FaqEntity faqEntity);

    boolean deleteByuKey(String uKey);

    ResponseEntity<Map<String,Object>> findAll(Pageable pageable);
}
