package com.secui.mvc.service;

import com.secui.mvc.entity.ArticleEntity;
import com.secui.mvc.request.ArticleRequestDto;
import com.secui.mvc.response.ArticleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleInterface {
    Page<ArticleResponseDto> findAll(Pageable pageable, String portal, String status, String search);

    boolean save(ArticleRequestDto articleRequestDto);

    ArticleResponseDto findByuKey(String uKey);

    ArticleEntity findByKey(String uKey);

    boolean update(ArticleRequestDto articleRequestDto, ArticleEntity articleEntity);

    boolean deleteByuKey(String uKey);

    boolean existsByPortalNameAndUrl(String portalName, String url);
}
