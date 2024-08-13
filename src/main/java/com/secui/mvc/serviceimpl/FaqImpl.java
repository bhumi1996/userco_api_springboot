package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.FaqEntity;
import com.secui.mvc.entity.ServiceEntity;
import com.secui.mvc.repository.FaqRepository;
import com.secui.mvc.request.FaqRequestDto;
import com.secui.mvc.response.FaqResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.FaqInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import com.secui.restapi.response.FaqResponse;
import com.secui.restapi.utility.RestConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqImpl implements FaqInterface {

    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;


    @Override
    public Page<FaqEntity> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(faqRepository.findAll(filterSearch.getFaqQuery(status,search),pageable),new TypeToken<Page<FaqResponseDto>>(){}.getType());
    }

    @Override
    public boolean save(FaqRequestDto faqRequestDto) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            FaqEntity faqEntity = new FaqEntity();
            faqEntity.setUKey(UtilHelper.uKey());
            faqEntity.setCreatedBy(userName);
            faqEntity.setLastModifiedBy(userName);
            mapping(faqEntity,faqRequestDto);
            faqRepository.save(faqEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in FaqImpl :: Method save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapping(FaqEntity faqEntity, FaqRequestDto faqRequestDto) {
      faqEntity.setFaqQuestion(faqRequestDto.getFaqQuestion());
      faqEntity.setFaqAnswer(faqRequestDto.getFaqAnswer());
      faqEntity.setStatus(faqRequestDto.getStatus());
    }

    @Override
    public boolean existsByFaqQuestion(String faqQuestion) {
        return faqRepository.existsByFaqQuestion(faqQuestion);
    }

    @Override
    public boolean existsByuKey(String uKey) {
        return faqRepository.existsByuKey(uKey);
    }

    @Override
    public FaqResponseDto findByuKey(String uKey) {
        FaqEntity faqEntity = faqRepository.findByuKey(uKey);
        if(faqEntity!=null){
            return modelMapper.map(faqEntity,FaqResponseDto.class);
        }
        return null;
    }

    @Override
    public FaqEntity findByKey(String uKey) {
        return faqRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(FaqRequestDto faqRequestDto, FaqEntity faqEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            faqEntity.setLastModifiedBy(userName);
            mapping(faqEntity,faqRequestDto);
            faqRepository.save(faqEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in FaqImpl :: Method update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            faqRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in FaqImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Pageable pageable) {
        try{

            Map<String,Object> result = new HashMap<>();
            Page<FaqEntity> pages = faqRepository.findAll(filterSearch.getFaqQuery(RestConstantUtil.ACTIVE,null),pageable);
            result.put(RestConstantUtil.LIST,createFaq(pages.getContent()));
            result.put(RestConstantUtil.TOTAL_ELEMENTS,pages.getTotalElements());
            result.put(RestConstantUtil.TOTAL_PAGES,pages.getNumber());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception in faqImpl :: findAll() {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<FaqResponse> createFaq(List<FaqEntity> faqs) {

        List<FaqResponse>faqResponses = new ArrayList<>();
        for (FaqEntity faqEntity : faqs){
            FaqResponse faqResponse = new FaqResponse();
            faqResponse.setFaqQuestion(faqEntity.getFaqQuestion());
            faqResponse.setFaqAnswer(faqEntity.getFaqAnswer());
            faqResponses.add(faqResponse);
        }
         return faqResponses;
    }
}
