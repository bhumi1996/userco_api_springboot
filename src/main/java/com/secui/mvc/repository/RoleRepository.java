package com.secui.mvc.repository;

import com.secui.mvc.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    boolean existsByName(String name);

    List<RoleEntity> findAllByStatus(String active);


    Page<RoleEntity> findAll(Specification<RoleEntity> roleQuery, Pageable pageable);

    RoleEntity findByName(String name);
}
