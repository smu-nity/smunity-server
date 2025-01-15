package com.smunity.server.global.common.entity;

import com.smunity.server.global.common.entity.enums.SubDomain;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private boolean isEditable;

    @Column(nullable = false)
    private int memberCount = 0;

    private String code;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Member> members = new ArrayList<>();
}
