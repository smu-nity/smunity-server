package com.smunity.server.domain.course.entity;

import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "core_standard")
public class Standard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private Year year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private int total;
}
