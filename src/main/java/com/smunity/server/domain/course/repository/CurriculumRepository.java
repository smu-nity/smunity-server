package com.smunity.server.domain.course.repository;

import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.global.common.entity.Year;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {

    List<Curriculum> findAllByYearAndDomain(Year year, Domain domain);
}
