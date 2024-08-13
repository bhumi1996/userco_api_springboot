package com.secui.mvc.repository;

import com.secui.mvc.entity.TestimonialEntity;
import com.secui.mvc.response.TestimonialResponseDto;
import com.secui.mvc.utility.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TestimonialRepository extends JpaRepository<TestimonialEntity,Long> {

    Page<TestimonialEntity> findAll(Specification<TestimonialEntity> testimonialQuery, Pageable pageable);

    boolean existsByNameAndDesignation(String name, String designation);

    boolean existsByuKey(String uKey);

    TestimonialEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    boolean existsByPortalNameAndNameAndDesignation(String portalName, String name, String designation);
    @Query(value = QueryUtils.FIND_FIRST_10_TESTIMONIALS, nativeQuery = true)
    List<Object[]> findFirst10ByPortalNameAndStatusOrderByLastModifiedDateDesc(String portal, String status);
}
