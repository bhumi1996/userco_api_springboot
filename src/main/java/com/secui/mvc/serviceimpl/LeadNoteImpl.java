package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.entity.NoteEntity;
import com.secui.mvc.repository.LeadNoteRepository;
import com.secui.mvc.repository.LeadRepository;
import com.secui.mvc.request.NoteRequestDto;
import com.secui.mvc.response.NoteResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.LeadNoteInterface;
import com.secui.mvc.utility.FilterSearch;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadNoteImpl implements LeadNoteInterface {
    private final LeadRepository leadRepository;
    private final LeadNoteRepository leadNoteRepository;
    private final FilterSearch filterSearch;
    private final ModelMapper mapper;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;

    @Override
    public List<NoteEntity> findAllByLead(LeadEntity leadEntity) {
        return leadNoteRepository.findAllByLeadEntityOrderByLastModifiedDateDesc(leadEntity);
    }

    @Override
    public boolean save(NoteRequestDto noteRequestDto) {
        try {
            NoteEntity noteEntity = new NoteEntity();
            noteEntity.setUKey(UtilHelper.uKey());
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            noteEntity.setCreatedBy(userName);
            noteEntity.setLastModifiedBy(userName);
            noteEntity.setLeadEntity(noteRequestDto.getLeadEntity());
            noteEntity.setHeading(noteRequestDto.getHeading());
            noteEntity.setMessage(noteRequestDto.getMessage());
            leadNoteRepository.save(noteEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in NoteImpl :: Method save() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public NoteResponseDto findByuKey(String uKey) {
        NoteEntity noteEntity = leadNoteRepository.findByuKey(uKey);
        if (noteEntity != null) {
            return mapper.map(noteEntity, NoteResponseDto.class);
        }
        return null;
    }

    @Override
    public NoteEntity findByKey(String uKey) {
        return leadNoteRepository.findByuKey(uKey);
    }

    @Override
    public boolean update(NoteRequestDto noteRequestDto, NoteEntity noteEntity) {
        try {
            String userName = currentUserAuthenticationServiceInterface.getCurrentUserName();
            noteEntity.setLastModifiedBy(userName);
            noteEntity.setHeading(noteRequestDto.getHeading());
            noteEntity.setMessage(noteRequestDto.getMessage());
            leadNoteRepository.save(noteEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in NoteImpl :: Method update() ::{}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByuKey(String uKey, LeadEntity leadEntity) {
        try {
            leadNoteRepository.deleteByuKey(uKey);
            leadRepository.save(leadEntity);
            return true;
        } catch (Exception ex) {
            log.error("Exception in NoteImpl :: Method deleteByuKey() ::{}", ex.getMessage());
            return false;
        }
    }
}
