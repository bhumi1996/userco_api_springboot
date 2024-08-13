package com.secui.mvc.service;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.entity.NoteEntity;
import com.secui.mvc.request.NoteRequestDto;
import com.secui.mvc.response.NoteResponseDto;

import java.util.List;

public interface LeadNoteInterface {
    List<NoteEntity> findAllByLead(LeadEntity leadEntity);

    boolean save(NoteRequestDto noteRequestDto);

    NoteResponseDto findByuKey(String uKey);

    NoteEntity findByKey(String uKey);

    boolean update(NoteRequestDto noteRequestDto, NoteEntity noteEntity);

    boolean deleteByuKey(String uKey, LeadEntity leadEntity);
}
