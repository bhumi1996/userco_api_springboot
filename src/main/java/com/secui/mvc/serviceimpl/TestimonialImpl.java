package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.TestimonialEntity;
import com.secui.mvc.repository.TestimonialRepository;
import com.secui.mvc.request.TestimonialRequestDto;
import com.secui.mvc.response.TestimonialResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.TestimonialInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import com.secui.restapi.response.TestimonialResponse;
import com.secui.restapi.utility.RestConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestimonialImpl implements TestimonialInterface {
    private final TestimonialRepository testimonialRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public Page<TestimonialResponseDto> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(testimonialRepository.findAll(filterSearch.getTestimonialQuery(status,search,null),pageable),new TypeToken<Page<TestimonialResponseDto>>(){}.getType());
    }

    @Override
    public boolean existByNameAndDesignation(String name, String designation) {
        return testimonialRepository.existsByNameAndDesignation(name,designation);
    }

    @Override
    public boolean save(TestimonialRequestDto testimonialRequestDto) {
        try {
            TestimonialEntity testimonialEntity = new TestimonialEntity();
            testimonialEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            testimonialEntity.setCreatedBy(userName);
            testimonialEntity.setLastModifiedBy(userName);
            mapper(testimonialEntity,testimonialRequestDto);
            testimonialRepository.save(testimonialEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in TestimonialImpl :: Method save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapper(TestimonialEntity testimonialEntity, TestimonialRequestDto testimonialRequestDto) {
     testimonialEntity.setPortalName(testimonialRequestDto.getPortalName());
     testimonialEntity.setName(testimonialRequestDto.getName());
     testimonialEntity.setComments(testimonialRequestDto.getComments());
     testimonialEntity.setDesignation(testimonialRequestDto.getDesignation());
     testimonialEntity.setLocation(testimonialRequestDto.getLocation());
     testimonialEntity.setStatus(testimonialRequestDto.getStatus());
    }

    @Override
    public boolean existsByuKey(String uKey) {
        return testimonialRepository.existsByuKey(uKey);
    }

    @Override
    public TestimonialResponseDto findByuKey(String uKey) {
        TestimonialEntity testimonialEntity = testimonialRepository.findByuKey(uKey);
        if(testimonialEntity!=null){
            return modelMapper.map(testimonialEntity,TestimonialResponseDto.class);
        }
        return null;
    }

    @Override
    public TestimonialEntity findByKey(String uKey) {
        return testimonialRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(TestimonialRequestDto testimonialRequestDto, TestimonialEntity testimonialEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            testimonialEntity.setLastModifiedBy(userName);
            mapper(testimonialEntity,testimonialRequestDto);
            testimonialRepository.save(testimonialEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in TestimonialImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            testimonialRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in TestimonialImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean existByPortalNameAndNameAndDesignation(String portalName, String name, String designation) {
        return testimonialRepository.existsByPortalNameAndNameAndDesignation(portalName,name,designation);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Pageable pageable, String portal) {
        try{

            Map<String,Object> result = new HashMap<>();
            Page<TestimonialEntity> pages = testimonialRepository.findAll(filterSearch.getTestimonialQuery(RestConstantUtil.ACTIVE,null,portal),pageable);
            result.put(RestConstantUtil.LIST,createTestimonial(pages.getContent()));
            result.put(RestConstantUtil.TOTAL_ELEMENTS,pages.getTotalElements());
            result.put(RestConstantUtil.TOTAL_PAGES,pages.getNumber());
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception in TestimonialImpl :: findAll() {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<TestimonialResponse> createTestimonial(List<TestimonialEntity> testimonialResponseDtos) {

        List<TestimonialResponse> testimonialResponses = new ArrayList<>();
        for(TestimonialEntity testimonialResponseDto:testimonialResponseDtos){
            TestimonialResponse testimonialResponse = new TestimonialResponse();
            testimonialResponse.setName(testimonialResponseDto.getName());
            testimonialResponse.setDesignation(testimonialResponseDto.getDesignation());
            testimonialResponse.setComments(testimonialResponseDto.getComments());
            if(testimonialResponseDto.getFileName()!=null && testimonialResponseDto.getFileName().isEmpty()){

            }
            else {
                testimonialResponse.setFileName(StringUtils.EMPTY);
            }
            testimonialResponses.add(testimonialResponse);
        }
        return testimonialResponses;
    }


    @Override
    public ResponseEntity<List<TestimonialResponse>> findAllTestimonial(String portal) {
        try {
            return new ResponseEntity<>(create(testimonialRepository.findFirst10ByPortalNameAndStatusOrderByLastModifiedDateDesc(portal, ConstantUtil.ACTIVE)), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception in TestimonialImpl :: findAllTestimonial() {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static List<TestimonialResponse> create(List<Object[]> listObjectArrayList) {
        List<TestimonialResponse> responseDtoList = new LinkedList<>();
        for (Object[] obj : listObjectArrayList) {
            TestimonialResponse testimonialResponseDto = new TestimonialResponse();
            testimonialResponseDto.setName((String) obj[0]);
            testimonialResponseDto.setDesignation((String) obj[1]);
            testimonialResponseDto.setComments((String) obj[2]);
            if (obj[4] != null && StringUtils.isNotBlank((String) obj[4])) {
//                testimonialResponseDto.setFileName(commonPropertiesUtil.getHostName().concat((String) obj[4]));
            } else {
                testimonialResponseDto.setFileName(StringUtils.EMPTY);
            }
            responseDtoList.add(testimonialResponseDto);
        }
        return responseDtoList;
    }

}
