package com.smunity.server.domain.department.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.global.common.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smunity.server.global.common.entity.QDepartment.department;

@Repository
@RequiredArgsConstructor
public class DepartmentQueryRepositoryImpl implements DepartmentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Department> findByIsEditable(Boolean isEditable) {
        return query.selectFrom(department)
                .where(isEditableEq(isEditable))
                .fetch();
    }

    private BooleanExpression isEditableEq(Boolean isEditable) {
        return isEditable != null ? department.isEditable.eq(isEditable) : null;
    }
}
