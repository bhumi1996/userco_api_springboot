package com.secui.repository;

import com.secui.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ModuleRepository extends JpaRepository<ModuleEntity,Long> {
    ModuleEntity findByName(String name);
}
