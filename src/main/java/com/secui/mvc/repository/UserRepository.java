package com.secui.mvc.repository;

import com.secui.mvc.entity.UserEntity;
import com.secui.mvc.utility.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmail(String email);

    UserEntity findByuKey(String uKey);

    void deleteByuKey(String uKey);

    Page<UserEntity> findAll(Specification<UserEntity> userQuery, Pageable pageable);

    UserEntity findByEmail(String username);

    @Query(value = QueryUtils.FAILED_ATTEMPT_QUERY, nativeQuery = true)
    @Modifying
    void updateFailedAttempts(int failAttempts, String email);
}
