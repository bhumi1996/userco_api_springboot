package com.secui.repository;

import com.secui.entity.CampaignTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CampaignTypeRepository extends JpaRepository<CampaignTypeEntity,Long> {
    boolean existsByType(String type);

    CampaignTypeEntity findByuKey(String uKey);

    boolean existsByuKey(String uKey);

    Page<CampaignTypeEntity> findAll(Specification<com.secui.entity.CampaignTypeEntity> typeQuery, Pageable pageable);

    void deleteByuKey(String uKey);
}
