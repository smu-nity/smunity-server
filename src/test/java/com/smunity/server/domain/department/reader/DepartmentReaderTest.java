package com.smunity.server.domain.department.reader;

import com.smunity.server.domain.department.dto.DepartmentResponse;
import com.smunity.server.domain.department.mapper.DepartmentMapperImpl;
import com.smunity.server.domain.department.repository.DepartmentQueryRepositoryImpl;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.DepartmentRepository;
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
@Import({QueryDslConfig.class, DepartmentQueryRepositoryImpl.class, DepartmentMapperImpl.class})
class DepartmentReaderTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentQueryRepositoryImpl departmentQueryRepository;
    @Autowired
    private DepartmentMapperImpl departmentMapper;

    private Long computer;
    private Long game;
    private Long design;

    @BeforeEach
    void setUp() {
        computer = persistDepartment("컴퓨터과학전공", "03005");
        game = persistDepartment("게임전공", "03006");      // 회원 없음
        design = persistDepartment("디자인전공", "03007");

        persistMember("202010001", computer, null);
        persistMember("202010002", computer, null);
        persistMember("202010003", design, null);
        persistMember("202010004", null, design);           // 복수전공만 디자인 → 집계 제외
        em.flush();
        em.clear();
    }

    @Test
    void GROUP_BY는_회원이_없는_학과를_포함해_주전공_기준으로_집계한다() {
        List<DepartmentResponse> responses = new GroupByDepartmentReader(departmentQueryRepository)
                .readDepartments().content();

        assertThat(responses).extracting(DepartmentResponse::name, DepartmentResponse::count)
                .containsExactly(
                        tuple("컴퓨터과학전공", 2),
                        tuple("게임전공", 0),
                        tuple("디자인전공", 1));
    }

    @Test
    void LAZY_LOADING은_GROUP_BY와_같은_결과를_반환한다() {
        List<DepartmentResponse> expected = new GroupByDepartmentReader(departmentQueryRepository)
                .readDepartments().content();

        List<DepartmentResponse> actual = new LazyLoadingDepartmentReader(departmentRepository)
                .readDepartments().content();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void DENORMALIZED는_스케줄러_갱신_후_GROUP_BY와_같은_결과를_반환한다() {
        departmentRepository.findAll().forEach(Department::updateMemberCount);
        em.flush();
        em.clear();
        List<DepartmentResponse> expected = new GroupByDepartmentReader(departmentQueryRepository)
                .readDepartments().content();

        List<DepartmentResponse> actual = new DenormalizedDepartmentReader(departmentRepository, departmentMapper)
                .readDepartments().content();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void DENORMALIZED는_스케줄러_갱신_전에는_이전_값을_반환한다() {
        List<DepartmentResponse> actual = new DenormalizedDepartmentReader(departmentRepository, departmentMapper)
                .readDepartments().content();

        assertThat(actual).extracting(DepartmentResponse::count).containsOnly(0);
    }

    private Long persistDepartment(String name, String code) {
        em.createNativeQuery("""
                        insert into common_department (college, name, type, is_editable, has_advanced, member_count, code)
                        values ('융합공과대학', :name, 'BALANCE_ENGINEER', false, true, 0, :code)
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
