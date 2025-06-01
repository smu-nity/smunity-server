package com.smunity.server.global.common.repository;

import com.smunity.server.global.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    long countByCreatedAtAfter(LocalDateTime createdAt);
}
