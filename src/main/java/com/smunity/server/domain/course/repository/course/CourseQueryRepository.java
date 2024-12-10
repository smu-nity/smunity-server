package com.smunity.server.domain.course.repository.course;

import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;

import java.util.List;

public interface CourseQueryRepository {

    List<Course> findByMemberIdAndCategory(Long memberId, Category category);
}
