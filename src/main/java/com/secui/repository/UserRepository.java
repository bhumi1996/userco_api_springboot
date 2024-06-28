package com.secui.repository;

import com.secui.entity.UserEntity;
import com.secui.utility.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);

    UserEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<UserEntity> findAll(Specification<com.secui.entity.UserEntity> userQuery, Pageable pageable);

    UserEntity findByEmail(String username);

    @Query(value = QueryUtils.FAILED_ATTEMPT_QUERY, nativeQuery = true)
    @Modifying
    void updateFailedAttempts(int failAttempts, String email);
}
