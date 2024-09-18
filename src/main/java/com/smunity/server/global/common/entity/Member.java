package com.smunity.server.global.common.entity;

import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.global.common.entity.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<Course> courses = new ArrayList<>();

    public void setInfo(Year year, Department department, String encodePw) {
        this.year = year;
        this.department = department;
        password = encodePw;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public int getCompletedCredits() {
        return courses.stream()
                .mapToInt(Course::getCredit)
                .sum();
    }
}
