package com.secui.mvc.repository;

import com.secui.mvc.entity.PrivilegeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity,Long> {
    PrivilegeEntity findByName(String name);
}
