package com.secui.service;

import com.secui.entity.ModuleEntity;

import java.util.List;

public interface ModuleInterface {
    List<ModuleEntity> findAll();

    ModuleEntity findByName(String module);

    void save(ModuleEntity moduleEntity);
}
