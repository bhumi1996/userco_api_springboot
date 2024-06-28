package com.secui.repository;

import com.secui.entity.GroupEntity;
import com.secui.utility.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<GroupEntity,Long> {
    boolean existsByGroupName(String groupName);

    GroupEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<GroupEntity> findAll(Specification<GroupEntity> groupQuery, Pageable pageable);

    List<GroupEntity> findAllByStatus(String status);

    @Query(value = QueryUtils.GROUPS_BASEDON_PORTAL_AND_UKEY_QUERY,nativeQuery = true)
    List<GroupEntity> findAllByuKey(List<String> uKeys);
}
