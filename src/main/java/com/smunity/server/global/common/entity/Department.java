package com.smunity.server.global.common.entity;

import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "common_department")
public class Department {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SubDomain subDomain;
}
