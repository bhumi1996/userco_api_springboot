package com.secui.repository;

import com.secui.entity.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CampaignRepository extends JpaRepository<CampaignEntity,Long> {
    boolean existsByName(String name);

    CampaignEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<CampaignEntity> findAll(Specification<CampaignEntity> campaignQuery, Pageable pageable);
}
