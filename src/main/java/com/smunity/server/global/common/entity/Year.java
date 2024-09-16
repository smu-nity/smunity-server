package com.smunity.server.global.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "common_year")
public class Year {

    @Id
    @Column(name = "year_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_name", nullable = false)
    private String name;

    @Column(name = "year_value")
    private Integer value;

    @Column(name = "major_i")
    private Integer majorI;

    @Column(name = "major_s")
    private Integer majorS;

    private Integer culture;

    private Integer cultureCnt;

    private Integer total;
}
