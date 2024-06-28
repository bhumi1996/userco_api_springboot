package com.secui.repository;

import com.secui.entity.PortalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PortalRepository extends JpaRepository<PortalEntity,Long> {
    PortalEntity findByuKey(String uKey);

    boolean existsByPortalName(String portalName);

    boolean existsByShortName(String shortName);

    boolean existsByDomainName(String domainName);

    void deleteByuKey(String uKey);

    Page<PortalEntity> findAll(Specification<com.secui.entity.PortalEntity> portalQuery, Pageable pageable);
}
