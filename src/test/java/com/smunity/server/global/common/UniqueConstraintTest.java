package com.smunity.server.global.common;

import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.global.config.QueryDslConfig;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import(QueryDslConfig.class)
class UniqueConstraintTest {

    @Autowired
    private EntityManager em;

    @Test
    void 같은_회원의_같은_과목번호는_중복_저장할_수_없다() {
        Member member = persistMember("202012345");
        persistCourse(member, "A0000001");

        assertThatThrownBy(() -> persistCourse(member, "A0000001"))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(java.sql.SQLException.class);
    }

    @Test
    void 다른_회원이거나_다른_과목번호는_저장할_수_있다() {
        Member member = persistMember("202012345");
        Member other = persistMember("202012346");
        persistCourse(member, "A0000001");

        assertThatCode(() -> {
            persistCourse(member, "A0000002");
            persistCourse(other, "A0000001");
        }).doesNotThrowAnyException();
    }

    @Test
    void 한_질문에_답변을_두_개_저장할_수_없다() {
        Member member = persistMember("202012345");
        Question question = Question.builder().title("제목").content("내용").anonymous(false).member(member).build();
        em.persist(question);
        persistAnswer(member, question);

        assertThatThrownBy(() -> persistAnswer(member, question))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(java.sql.SQLException.class);
    }

    private Member persistMember(String username) {
        Member member = Member.builder().username(username).password("pw").email("a@b.c").name("name").build();
        em.persist(member);
        return member;
    }

    private void persistCourse(Member member, String number) {
        em.persist(Course.builder()
                .member(member).name("과목").number(number).year("2024").semester("1학기")
                .type("전공").category(Category.MAJOR_OPTIONAL).credit(3)
                .build());
        em.flush();
    }

    private void persistAnswer(Member member, Question question) {
        em.persist(Answer.builder().content("답변").member(member).question(question).build());
        em.flush();
    }
}
