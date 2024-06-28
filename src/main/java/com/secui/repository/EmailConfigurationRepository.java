package com.secui.repository;

import com.secui.entity.EmailConfigurationEntity;
import com.secui.response.EmailConfigurationResponseDto;
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

    EmailConfigurationResponseDto findByStatus(String active);

    EmailConfigurationEntity findByName(String emailConfiguration);
}
