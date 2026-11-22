package com.smunity.server.domain.department.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.department.dto.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smunity.server.global.common.entity.QDepartment.department;
import static com.smunity.server.global.common.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class DepartmentQueryRepositoryImpl implements DepartmentQueryRepository {

    private final JPAQueryFactory query;

    /**
     * 학과별 회원 수를 단일 쿼리로 집계한다.
     * 기존 members 컬렉션(mappedBy = "department")과 동일하게 주전공 기준으로 집계하며,
     * 회원이 없는 학과도 0명으로 포함하기 위해 left join을 사용한다.
     */
    @Override
    public List<DepartmentResponse> findAllWithMemberCount() {
        return query.select(Projections.constructor(DepartmentResponse.class,
                        department.college,
                        department.name,
                        department.code,
                        member.id.count().intValue()))
                .from(department)
                .leftJoin(member).on(member.department.eq(department))
                .groupBy(department.id, department.college, department.name, department.code)
                .orderBy(department.id.asc())
                .fetch();
    }
}
