package com.smunity.server.domain.department.repository;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import({QueryDslConfig.class, DepartmentQueryRepositoryImpl.class})
class DepartmentQueryRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private DepartmentQueryRepository departmentQueryRepository;

    @BeforeEach
    void setUp() {
        Long computer = persistDepartment("컴퓨터과학전공", "03005");
        persistDepartment("게임전공", "03006");                // 회원 없음
        Long design = persistDepartment("디자인전공", "03007");

        persistMember("202010001", computer, null);
        persistMember("202010002", computer, null);
        persistMember("202010003", design, null);
        persistMember("202010004", null, design);              // 복수전공만 디자인 → 집계 제외
        em.flush();
        em.clear();
    }

    @Test
    void 회원이_없는_학과를_포함해_주전공_기준으로_학과별_회원_수를_집계한다() {
        List<DepartmentResponse> responses = departmentQueryRepository.findAllWithMemberCount();

        assertThat(responses).extracting(DepartmentResponse::name, DepartmentResponse::count)
                .containsExactly(
                        tuple("컴퓨터과학전공", 2),
                        tuple("게임전공", 0),
                        tuple("디자인전공", 1));
    }

    private Long persistDepartment(String name, String code) {
        em.createNativeQuery("""
                        insert into common_department (college, name, type, is_editable, has_advanced, code)
                        values ('융합공과대학', :name, 'BALANCE_ENGINEER', false, true, :code)
                        """)
                .setParameter("name", name)
                .setParameter("code", code)
                .executeUpdate();
        return ((Number) em.createNativeQuery("select department_id from common_department where code = :code")
                .setParameter("code", code)
                .getSingleResult()).longValue();
    }

    private void persistMember(String username, Long departmentId, Long secondDepartmentId) {
        Member member = Member.builder()
                .username(username).password("pw").email("a@b.c").name("name")
                .secondDepartment(secondDepartmentId != null ? em.find(Department.class, secondDepartmentId) : null)
                .build();
        if (departmentId != null) {
            member.setDepartment(em.find(Department.class, departmentId));
        }
        em.persist(member);
    }
}
