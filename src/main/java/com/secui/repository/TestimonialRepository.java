package com.secui.repository;

import com.secui.entity.TestimonialEntity;
import com.secui.response.TestimonialResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TestimonialRepository extends JpaRepository<TestimonialEntity,Long> {

    Page<TestimonialResponseDto> findAll(Specification<TestimonialEntity> testimonialQuery, Pageable pageable);

    boolean existsByNameAndDesignation(String name, String designation);

    boolean existsByuKey(String uKey);

    TestimonialEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);
}
