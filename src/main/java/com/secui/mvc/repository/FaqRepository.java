package com.secui.mvc.repository;

import com.secui.mvc.entity.FaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FaqRepository extends JpaRepository<FaqEntity,Long> {
    boolean existsByFaqQuestion(String faqQuestion);

    boolean existsByuKey(String uKey);

    FaqEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<FaqEntity> findAll(Specification<FaqEntity> faqQuery, Pageable pageable);
}
