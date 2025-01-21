package com.smunity.server.global.common.entity;

import com.smunity.server.global.common.entity.enums.Semester;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "common_term")
public class Term {

    @Id
    @Column(name = "term_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_value")
    private Integer year;

    @Enumerated(EnumType.STRING)
    private Semester semester;
}
