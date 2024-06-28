package com.secui.repository;

import com.secui.entity.LeadEntity;
import com.secui.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LeadNoteRepository extends JpaRepository<NoteEntity,Long> {
    List<NoteEntity> findAllByLeadEntityOrderByLastModifiedDateDesc(LeadEntity leadEntity);

    NoteEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);
}
