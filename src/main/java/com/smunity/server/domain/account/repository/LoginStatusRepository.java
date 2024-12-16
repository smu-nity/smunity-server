package com.smunity.server.domain.account.repository;

import com.smunity.server.domain.account.entity.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginStatusRepository extends JpaRepository<LoginStatus, Long> {
    
}
