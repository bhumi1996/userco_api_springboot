package com.secui.mvc.repository;

import com.secui.mvc.entity.PortalEntity;
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
public interface PortalRepository extends JpaRepository<PortalEntity,Long> {
    PortalEntity findByuKey(String uKey);

    boolean existsByPortalName(String portalName);

    boolean existsByShortName(String shortName);

    boolean existsByDomainName(String domainName);

    void deleteByuKey(String uKey);

    Page<PortalEntity> findAll(Specification<PortalEntity> portalQuery, Pageable pageable);

    @Query(value = QueryUtils.PORTAL_SHORT_NAME_QUERY,nativeQuery = true)
    List<String> findAllByStatus(String active);

}
