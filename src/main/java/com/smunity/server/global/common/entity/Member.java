package com.smunity.server.global.common.entity;

import com.smunity.server.domain.account.entity.LoginStatus;
import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.Exemption;
import com.smunity.server.global.common.entity.enums.MemberRole;
import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.smunity.server.global.common.entity.enums.SubDomain.BALANCE_NATURAL_ENGINEER;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_department_id")
    private Department secondDepartment;

    @Enumerated(EnumType.STRING)
    private Exemption exemption;

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
        department.updateMemberCount();
    }

    public void setInfo(Year year, Department department, Department secondDepartment, String encodePw) {
        this.year = year;
        setDepartment(department);
        this.secondDepartment = secondDepartment;
        password = encodePw;
    }

    public void update(Department department, Department secondDepartment, String name, String email) {
        this.department = department;
        this.secondDepartment = secondDepartment;
        this.name = name;
        this.email = email;
        courses.clear();
    }

    public void delete() {
        Department department = this.department;
        this.department = null;
        department.getMembers().remove(this);
        department.updateMemberCount();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDepartment(Department department) {
        this.department = department;
    }

    public void changeExemption(Exemption exemption) {
        this.exemption = exemption;
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
        return isNewCurriculum() && subDomain.isNaturalOrEngineer() ? BALANCE_NATURAL_ENGINEER : subDomain;
    }

    public boolean checkCompleted(SubDomain subDomain) {
        return courses.stream()
                .map(Course::getSubDomain)
                .anyMatch(subDomain::equals);
    }

    public boolean isNewCurriculum() {
        return year.getValue() >= 2024;
    }

    public boolean isDoubleMajor() {
        return secondDepartment != null;
    }
}
