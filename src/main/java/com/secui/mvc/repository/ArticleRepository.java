package com.secui.mvc.repository;

import com.secui.mvc.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity,Long> {
    void deleteByuKey(String uKey);

    ArticleEntity findByuKey(String uKey);

    Page<ArticleEntity> findAll(Specification<ArticleEntity> articleQuery, Pageable pageable);

    boolean existsByPortalNameAndUrl(String portalName, String url);
}
