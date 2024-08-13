package com.secui.mvc.repository;

import com.secui.mvc.entity.LeadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LeadRepository extends JpaRepository<LeadEntity,Long> {
    boolean existsByEmail(String email);

    LeadEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);


    Page<LeadEntity> findAll(Specification<LeadEntity> leadQuery, Pageable pageable);
}
