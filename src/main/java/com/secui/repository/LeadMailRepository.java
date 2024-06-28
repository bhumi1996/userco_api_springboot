package com.secui.repository;

import com.secui.entity.LeadEntity;
import com.secui.entity.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LeadMailRepository extends JpaRepository<MailEntity,Long> {
    List<MailEntity> findAllByLeadEntityOrderByLastModifiedDateDesc(LeadEntity leadEntity);
}
