package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.CountryEntity;
import com.secui.mvc.repository.CountryRepository;
import com.secui.mvc.request.CountryRequestDto;
import com.secui.mvc.response.CountryResponseDto;
import com.secui.mvc.service.CountryInterface;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryInterfaceImpl implements CountryInterface {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final FilterSearch filterSearch;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }

    @Override
    public boolean existsByIsoTwo(String isoTwo) {
        return countryRepository.existsByIsoTwo(isoTwo);
    }

    @Override
    public boolean existsByIsoThree(String isoThree) {
        return countryRepository.existsByIsoThree(isoThree);
    }

    @Override
    public boolean existsByIsdCode(String isdCode) {
        return countryRepository.existsByIsdCode(isdCode);
    }

    @Override
    public boolean save(CountryRequestDto countryRequestDto) {
        try {
            CountryEntity countryEntity = new CountryEntity();
            mapping(countryEntity, countryRequestDto);
            countryEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            countryEntity.setCreatedBy(userName);
            countryEntity.setLastModifiedBy(userName);
            countryRepository.save(countryEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CountryImpl :: Method Save() ::{} ", exception.getMessage());
            return false;
        }
    }

    private void mapping(CountryEntity countryEntity, CountryRequestDto countryRequestDto) {
        countryEntity.setName(countryRequestDto.getName());
        countryEntity.setCapital(countryRequestDto.getCapital());
        countryEntity.setIsoTwo(countryRequestDto.getIsoTwo());
        countryEntity.setIsoThree(countryRequestDto.getIsoThree());
        countryEntity.setIsdCode(countryRequestDto.getIsdCode());
        countryEntity.setStatus(countryRequestDto.getStatus());
    }

    @Override
    public CountryResponseDto findByuKey(String uKey) {
        CountryEntity countryEntity = countryRepository.findByuKey(uKey);
        if (countryEntity != null) {
            return modelMapper.map(countryEntity, CountryResponseDto.class);
        }
        return null;
    }

    @Override
    public CountryEntity findByKey(String uKey) {
        return countryRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(CountryRequestDto countryRequestDto, CountryEntity countryEntity) {
        try {
            mapping(countryEntity, countryRequestDto);
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            countryEntity.setLastModifiedBy(userName);
            countryRepository.save(countryEntity);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CountryImpl :: Method Update() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey) {
        try {
            countryRepository.deleteByuKey(uKey);
            return true;
        } catch (Exception exception) {
            log.error("Exception in CountryImpl :: Method Delete() ::{} ", exception.getMessage());
            return false;
        }
    }

    @Override
    public Page<CountryResponseDto> findAll(Pageable pageable, String status, String search) {
        return modelMapper.map(countryRepository.findAll(filterSearch.getCountryQuery(status, search), pageable), new TypeToken<Page<CountryResponseDto>>() {
        }.getType());
    }

    @Override
    public List<CountryResponseDto> getActiveCountry() {
        return countryRepository.findAllByStatus(ConstantUtil.ACTIVE);
    }

    @Override
    public boolean existsByNameAndStatus(String country) {
        return countryRepository.existsByNameAndStatus(country,ConstantUtil.ACTIVE);
    }

    @Override
    public boolean existsByIsdCodeAndStatus(String dialCode) {
        return countryRepository.existsByIsdCodeAndStatus(dialCode,ConstantUtil.ACTIVE);
    }


}
