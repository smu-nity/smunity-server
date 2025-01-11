package com.smunity.server.global.common.entity;

import com.smunity.server.domain.account.entity.LoginStatus;
import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.smunity.server.global.common.entity.enums.SubDomain.*;

@Entity
@Getter
@Builder
@Table(name = "common_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.ROLE_USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private Year year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<LoginStatus> loginStatuses = new ArrayList<>();

    public void setDepartment(Department department) {
        this.department = department;
        department.getMembers().add(this);
    }

    public void setInfo(Year year, Department department, String encodePw) {
        this.year = year;
        setDepartment(department);
        password = encodePw;
    }

    public void update(Department department, String name, String email) {
        this.department = department;
        this.name = name;
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDepartment(Department department) {
        this.department = department;
    }

    public int getCompletedCredits() {
        return courses.stream()
                .mapToInt(Course::getCredit)
                .sum();
    }

    public int getCompletedCredits(Category category) {
        return courses.stream()
                .filter(course -> course.getCategory().equals(category))
                .mapToInt(Course::getCredit)
                .sum();
    }

    public List<String> getCompletedNumbers() {
        return courses.stream()
                .map(Course::getNumber)
                .toList();
    }

    public SubDomain getSubDomain() {
        SubDomain subDomain = department.getSubDomain();
        return year.getValue() >= 2024 && (subDomain.equals(BALANCE_NATURAL) || subDomain.equals(BALANCE_ENGINEER)) ? BALANCE_NATURAL_ENGINEER : subDomain;
    }

    public boolean checkCompleted(SubDomain subDomain) {
        return courses.stream()
                .map(Course::getSubDomain)
                .anyMatch(subDomain::equals);
    }
}
