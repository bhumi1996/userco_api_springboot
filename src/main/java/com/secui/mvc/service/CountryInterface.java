package com.secui.mvc.service;

import com.secui.mvc.entity.CountryEntity;
import com.secui.mvc.request.CountryRequestDto;
import com.secui.mvc.response.CountryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryInterface {

    boolean existsByName(String name);

    boolean existsByIsoTwo(String isoTwo);

    boolean existsByIsoThree(String isoThree);

    boolean existsByIsdCode(String isdCode);

    boolean save(CountryRequestDto countryRequestDto);
    CountryResponseDto findByuKey(String uKey);

    CountryEntity findByKey(String uKey);

    boolean update(CountryRequestDto countryRequestDto, CountryEntity countryEntity);


    boolean deleteByuKey(String uKey);

    Page<CountryResponseDto> findAll(Pageable pageable, String s, String s1);

    List<CountryResponseDto> getActiveCountry();

    boolean existsByNameAndStatus(String country);

    boolean existsByIsdCodeAndStatus(String dialCode);


}
