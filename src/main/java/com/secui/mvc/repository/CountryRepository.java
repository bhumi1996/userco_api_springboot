package com.secui.mvc.repository;

import com.secui.mvc.entity.CountryEntity;
import com.secui.mvc.response.CountryResponseDto;
import com.secui.mvc.utility.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {


    boolean existsByName(String name);

    boolean existsByIsoTwo(String isoTwo);

    boolean existsByIsoThree(String isoThree);

    boolean existsByIsdCode(String isdCode);

    CountryEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<CountryEntity> findAll(Specification<CountryResponseDto> countryQuery, Pageable pageable);


    @Query(QueryUtils.COUNTRY_QUERY)
    List<CountryResponseDto> findAllByStatus(String status);

    boolean existsByNameAndStatus(String country,String status);

    boolean existsByIsdCodeAndStatus(String dialCode, String active);
}
