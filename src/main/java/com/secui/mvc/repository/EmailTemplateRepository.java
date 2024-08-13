package com.secui.mvc.repository;

import com.secui.mvc.entity.EmailTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity,Long> {

    boolean existsByTemplateName(String templateName);

    EmailTemplateEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<EmailTemplateEntity> findAll(Specification<EmailTemplateEntity> templateQuery, Pageable pageable);

    EmailTemplateEntity findByTemplateNameAndStatus(String leadMail, String active);
}
