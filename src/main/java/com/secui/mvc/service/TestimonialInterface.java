package com.secui.mvc.service;

import com.secui.mvc.entity.TestimonialEntity;
import com.secui.mvc.request.TestimonialRequestDto;
import com.secui.mvc.response.TestimonialResponseDto;
import com.secui.restapi.response.TestimonialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TestimonialInterface {

    Page<TestimonialResponseDto> findAll(Pageable pageable, String status, String search);

    boolean existByNameAndDesignation(String name, String designation);

    boolean save(TestimonialRequestDto testimonialRequestDto);

    boolean existsByuKey(String uKey);

    TestimonialResponseDto findByuKey(String uKey);

    TestimonialEntity findByKey(String uKey);

    boolean update(TestimonialRequestDto testimonialRequestDto, TestimonialEntity testimonialEntity);

    boolean deleteByuKey(String uKey);

    boolean existByPortalNameAndNameAndDesignation(String portalName, String name, String designation);
    ResponseEntity<Map<String,Object>> findAll(Pageable pageable, String portal);

    ResponseEntity<List<TestimonialResponse>> findAllTestimonial(String portal);
}
