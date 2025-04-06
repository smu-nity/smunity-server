package com.smunity.server.global.common.repository;

import com.smunity.server.global.common.entity.Year;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YearRepository extends JpaRepository<Year, Long> {

    Optional<Year> findByValue(Integer value);
}
