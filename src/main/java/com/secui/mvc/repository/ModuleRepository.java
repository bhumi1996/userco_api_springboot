package com.secui.mvc.repository;

import com.secui.mvc.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ModuleRepository extends JpaRepository<ModuleEntity,Long> {
    ModuleEntity findByName(String name);
}
