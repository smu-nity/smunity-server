package com.smunity.server.domain.course.repository.course;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.Category;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.smunity.server.domain.course.entity.QCourse.course;

@RequiredArgsConstructor
public class CourseQueryRepositoryImpl implements CourseQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Course> findByMemberIdAndCategory(Long memberId, Category category) {
        return query.selectFrom(course)
                .where(
                        memberIdEq(memberId),
                        categoryEq(category)
                )
                .fetch();
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? course.member.id.eq(id) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? course.category.eq(category) : null;
    }
}
