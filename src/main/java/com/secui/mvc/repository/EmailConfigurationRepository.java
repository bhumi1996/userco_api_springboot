package com.secui.mvc.repository;

import com.secui.mvc.entity.EmailConfigurationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface EmailConfigurationRepository extends JpaRepository<EmailConfigurationEntity,Long> {
    EmailConfigurationEntity findByuKey(String uKey);

    boolean existsByName(String name);

    void deleteByuKey(String uKey);

    Page<EmailConfigurationEntity> findAll(Specification<EmailConfigurationEntity> configurationQuery, Pageable pageable);

    EmailConfigurationEntity findByStatus(String active);

    EmailConfigurationEntity findByName(String emailConfiguration);

    EmailConfigurationEntity findByPortalNameAndStatus(String portalName, String active);

    boolean existsByPortalName(String portalName);
}
