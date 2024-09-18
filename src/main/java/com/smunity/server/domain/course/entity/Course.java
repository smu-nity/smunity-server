package com.smunity.server.domain.course.entity;

import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "core_course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "subject_name", nullable = false)
    private String name;

    @Column(name = "subject_number", nullable = false, length = 8)
    private String number;

    @Column(name = "subject_year", nullable = false)
    private String year;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String type;

    private String domain;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SubDomain subDomain;

    @Column(nullable = false)
    private int credit;

    public void setMember(Member member) {
        this.member = member;
        member.getCourses().add(this);
    }
}
