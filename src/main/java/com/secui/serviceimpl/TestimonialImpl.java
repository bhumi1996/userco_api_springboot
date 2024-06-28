package com.secui.serviceimpl;

import com.secui.entity.TestimonialEntity;
import com.secui.repository.TestimonialRepository;
import com.secui.request.TestimonialRequestDto;
import com.secui.response.TestimonialResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.TestimonialInterface;
import com.secui.utility.FilterSearch;
import com.secui.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return modelMapper.map(testimonialRepository.findAll(filterSearch.getTestimonialQuery(status,search),pageable),new TypeToken<Page<TestimonialResponseDto>>(){}.getType());
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
     testimonialEntity.setName(testimonialRequestDto.getName());
     testimonialEntity.setComments(testimonialRequestDto.getComments());
     testimonialEntity.setDesignation(testimonialRequestDto.getComments());
     testimonialEntity.setLocation(testimonialRequestDto.getLocation());
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
}
