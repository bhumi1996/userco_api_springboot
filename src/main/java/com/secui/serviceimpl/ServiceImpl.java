package com.secui.serviceimpl;

import com.secui.entity.ServiceEntity;
import com.secui.repository.ServiceRepository;
import com.secui.request.ServiceRequestDto;
import com.secui.response.ServiceResponseDto;
import com.secui.service.CurrentUserAuthenticationServiceInterface;
import com.secui.service.ServiceInterface;
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
