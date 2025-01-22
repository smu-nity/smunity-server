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

    private Integer total;
}
