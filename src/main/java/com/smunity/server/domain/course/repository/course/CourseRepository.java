package com.smunity.server.domain.course.repository.course;

import com.smunity.server.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseQueryRepository {

}
