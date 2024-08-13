package com.secui.mvc.repository;

import com.secui.mvc.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity,Long> {
    boolean existsByServiceName(String serviceName);

    void deleteByuKey(String uKey);

    ServiceEntity findByuKey(String uKey);

    boolean existsByuKey(String uKey);

    Page<ServiceEntity> findAll(Specification<ServiceEntity> serviceQuery, Pageable pageable);
}
