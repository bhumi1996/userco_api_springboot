package com.secui.service;

import com.secui.entity.TestimonialEntity;
import com.secui.request.TestimonialRequestDto;
import com.secui.response.TestimonialResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestimonialInterface {

    Page<TestimonialResponseDto> findAll(Pageable pageable, String status, String search);

    boolean existByNameAndDesignation(String name, String designation);

    boolean save(TestimonialRequestDto testimonialRequestDto);

    boolean existsByuKey(String uKey);

    TestimonialResponseDto findByuKey(String uKey);

    TestimonialEntity findByKey(String uKey);

    boolean update(TestimonialRequestDto testimonialRequestDto, TestimonialEntity testimonialEntity);

    boolean deleteByuKey(String uKey);
}
