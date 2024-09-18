package com.smunity.server.domain.course.repository;

import com.smunity.server.domain.course.entity.Standard;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StandardRepository extends JpaRepository<Standard, Long> {

    Optional<Standard> findByYearAndCategory(Year year, Category category);
}
