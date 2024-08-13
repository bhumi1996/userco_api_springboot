package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.ServiceEntity;
import com.secui.mvc.entity.TestimonialEntity;
import com.secui.mvc.repository.ServiceRepository;
import com.secui.mvc.request.ServiceRequestDto;
import com.secui.mvc.response.ServiceResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.ServiceInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import com.secui.restapi.response.ServiceResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements ServiceInterface {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public Page<ServiceResponseDto> findAll(Pageable pageable, String status, String search) {
     return modelMapper.map(serviceRepository.findAll(filterSearch.getServiceQuery(status,search),pageable),new TypeToken<Page<ServiceResponseDto>>(){}.getType());
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Pageable pageable) {
        try{

            Map<String,Object> result = new HashMap<>();
            Page<ServiceEntity> pages = serviceRepository.findAll(filterSearch.getServiceQuery(RestConstantUtil.ACTIVE,null),pageable);
            result.put(RestConstantUtil.LIST,createService(pages.getContent()));
            result.put(RestConstantUtil.TOTAL_ELEMENTS,pages.getTotalElements());
            result.put(RestConstantUtil.TOTAL_PAGES,pages.getNumber());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception in ServiceImpl :: findAll() {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<ServiceResponse> createService(List<ServiceEntity> serviceEntities) {
        List<ServiceResponse> serviceResponses = new ArrayList<>();
        for (ServiceEntity serviceEntity : serviceEntities) {
            ServiceResponse serviceResponse = new ServiceResponse();
            serviceResponse.setServiceName(serviceEntity.getServiceName());
            serviceResponse.setDescription(serviceEntity.getDescription());
            if (serviceEntity.getImageUrl() != null && !serviceEntity.getImageUrl().isEmpty()) {
            } else {
                serviceResponse.setImageUrl(StringUtils.EMPTY);
            }
            serviceResponses.add(serviceResponse);
        }

        return serviceResponses;
    }

    @Override
    public boolean save(ServiceRequestDto serviceRequestDto) {
        try {
           ServiceEntity serviceEntity = new ServiceEntity();
           serviceEntity.setUKey(UtilHelper.uKey());
           String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
           serviceEntity.setLastModifiedBy(userName);
           serviceEntity.setCreatedBy(userName);
           mapping(serviceEntity,serviceRequestDto);
           serviceRepository.save(serviceEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in RoleImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapping(ServiceEntity serviceEntity, ServiceRequestDto serviceRequestDto) {
     serviceEntity.setServiceName(serviceRequestDto.getServiceName());
     serviceEntity.setDescription(serviceRequestDto.getDescription());
     serviceEntity.setStatus(serviceRequestDto.getStatus());
    }

    @Override
    public boolean existsByuKey(String uKey) {
        return serviceRepository.existsByuKey(uKey);
    }

    @Override
    public ServiceResponseDto findByuKey(String uKey) {
        ServiceEntity serviceEntity = serviceRepository.findByuKey(uKey);
        if(serviceEntity!=null){
            return modelMapper.map(serviceEntity,ServiceResponseDto.class);
        }
        return null;
    }

    @Override
    public ServiceEntity findByKey(String uKey) {
        return serviceRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(ServiceRequestDto serviceRequestDto, ServiceEntity serviceEntity) {
            try {

                String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
                serviceEntity.setLastModifiedBy(userName);
                mapping(serviceEntity,serviceRequestDto);
                serviceRepository.save(serviceEntity);
                return true;
            } catch (Exception exception) {
                log.error("Exception in RoleImpl :: Method update() ::{} ", exception.getMessage());
                return false;
            }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            serviceRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in ServiceImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean existsByServiceName(String serviceName) {
        return serviceRepository.existsByServiceName(serviceName);
    }
}
