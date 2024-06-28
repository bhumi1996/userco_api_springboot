package com.secui.service;

import com.secui.entity.LeadEntity;
import com.secui.entity.NoteEntity;
import com.secui.request.NoteRequestDto;
import com.secui.response.NoteResponseDto;

import java.util.List;

public interface LeadNoteInterface {
    List<NoteEntity> findAllByLead(LeadEntity leadEntity);

    boolean save(NoteRequestDto noteRequestDto);

    NoteResponseDto findByuKey(String uKey);

    NoteEntity findByKey(String uKey);

    boolean update(NoteRequestDto noteRequestDto, NoteEntity noteEntity);

    boolean deleteByuKey(String uKey, LeadEntity leadEntity);
}
