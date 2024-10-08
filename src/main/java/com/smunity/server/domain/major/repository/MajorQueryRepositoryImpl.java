package com.smunity.server.domain.major.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smunity.server.domain.major.entity.QMajor.major;

@Repository
@RequiredArgsConstructor
public class MajorQueryRepositoryImpl implements MajorQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Major> findByDepartmentAndCategory(Department department, Category category, List<String> completedNumbers) {
        return query.selectFrom(major)
                .where(
                        departmentEq(department),
                        categoryEq(category),
                        numberNotIn(completedNumbers)
                )
                .fetch();
    }

    private BooleanExpression departmentEq(Department department) {
        return department != null ? major.department.eq(department) : null;
    }

    private BooleanExpression categoryEq(Category category) {
        return category != null ? major.category.eq(category) : null;
    }

    private BooleanExpression numberNotIn(List<String> numbers) {
        return numbers != null && !numbers.isEmpty() ? major.number.notIn(numbers) : null;
    }
}
