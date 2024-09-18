package com.smunity.server.domain.course.entity;

import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "core_curriculum")
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private Year year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Domain domain;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubDomain subDomain;
}
