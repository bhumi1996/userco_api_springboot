package com.secui.serviceimpl;

import com.secui.entity.ModuleEntity;
import com.secui.repository.ModuleRepository;
import com.secui.service.ModuleInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ModuleImpl implements ModuleInterface {
    private final ModuleRepository moduleRepository;

    @Override
    public List<ModuleEntity> findAll() {
        return moduleRepository.findAll();
    }

    @Override
    public ModuleEntity findByName(String name) {
        return moduleRepository.findByName(name);
    }

    @Override
    public void save(ModuleEntity moduleEntity) {
        moduleRepository.save(moduleEntity);
    }
}
