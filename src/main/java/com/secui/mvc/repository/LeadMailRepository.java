package com.secui.mvc.repository;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.entity.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LeadMailRepository extends JpaRepository<MailEntity,Long> {
    List<MailEntity> findAllByLeadEntityOrderByLastModifiedDateDesc(LeadEntity leadEntity);
}
