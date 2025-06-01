package com.smunity.server.domain.account.repository;

import com.smunity.server.domain.account.entity.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LoginStatusRepository extends JpaRepository<LoginStatus, Long> {

    long countDistinctMemberByLoginAtAfter(LocalDateTime loginAt);
}
