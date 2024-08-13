package com.secui.mvc.service;

import com.secui.mvc.entity.ModuleEntity;

import java.util.List;

public interface ModuleInterface {
    List<ModuleEntity> findAll();

    ModuleEntity findByName(String module);

    void save(ModuleEntity moduleEntity);
}
