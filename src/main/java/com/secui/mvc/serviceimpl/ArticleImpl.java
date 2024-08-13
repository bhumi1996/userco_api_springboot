package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.ArticleEntity;
import com.secui.mvc.repository.ArticleRepository;
import com.secui.mvc.request.ArticleRequestDto;
import com.secui.mvc.response.ArticleResponseDto;
import com.secui.mvc.service.ArticleInterface;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.utility.ColumnUtils;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleImpl implements ArticleInterface {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public Page<ArticleResponseDto> findAll(Pageable pageable, String portal, String status, String search) {
        return modelMapper.map(articleRepository.findAll(filterSearch.getArticleQuery(portal,status,search),pageable),new TypeToken<Page<ArticleResponseDto>>(){}.getType());
    }

    @Override
    public boolean save(ArticleRequestDto articleRequestDto) {
        try {
            ArticleEntity articleEntity = new ArticleEntity();
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            articleEntity.setCreatedBy(userName);
            articleEntity.setLastModifiedBy(userName);
            articleEntity.setUKey(UtilHelper.uKey());
            mapping(articleEntity,articleRequestDto);
            articleRepository.save(articleEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in articleImpl :: Method save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapping(ArticleEntity articleEntity, ArticleRequestDto articleRequestDto) {

        articleEntity.setHeading(articleRequestDto.getHeading());
        articleEntity.setPortalName(articleRequestDto.getPortalName());
        articleEntity.setUrl(articleRequestDto.getUrl());
        articleEntity.setTitle(articleRequestDto.getTitle());
        articleEntity.setDescription(articleRequestDto.getUrl());
        articleEntity.setShortDescription(articleRequestDto.getShortDescription());
        articleEntity.setStatus(articleRequestDto.getStatus());
    }

    @Override
    public ArticleResponseDto findByuKey(String uKey) {
        ArticleEntity articleEntity = articleRepository.findByuKey(uKey);
        if(articleEntity!=null){
            return modelMapper.map(articleEntity, ArticleResponseDto.class);
        }
        return null;
    }

    @Override
    public ArticleEntity findByKey(String uKey) {
        return articleRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(ArticleRequestDto articleRequestDto, ArticleEntity articleEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            articleEntity.setLastModifiedBy(userName);
            mapping(articleEntity,articleRequestDto);
            articleRepository.save(articleEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in articleImpl :: Method update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            articleRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in articleImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByPortalNameAndUrl(String portalName, String url) {
        return articleRepository.existsByPortalNameAndUrl(portalName,url);
    }
}
