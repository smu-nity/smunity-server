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
    public List<Course> findByMemberIdAndCategories(Long memberId, List<Category> categories) {
        return query.selectFrom(course)
                .where(
                        memberIdEq(memberId),
                        categoryIn(categories)
                )
                .fetch();
    }

    private BooleanExpression memberIdEq(Long id) {
        return id != null ? course.member.id.eq(id) : null;
    }

    private BooleanExpression categoryIn(List<Category> categories) {
        return categories != null && !categories.isEmpty() ? course.category.in(categories) : null;
    }
}
