package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.entity.Standard;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.repository.StandardRepository;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StandardService {

    private final StandardRepository standardRepository;

    public int getCultureTotal(int size, Domain domain) {
        return switch (domain) {
            case CORE -> 2;
            case BALANCE -> 3;
            default -> size;
        };
    }

    public int getTotal(Year year, Department department, Category category) {
        int total = getTotal(year, category);
        return department.isHasAdvanced() || category == null ? total : getTotal(total, category);
    }

    public int getTotal(Year year, Category category) {
        return standardRepository.findByYearAndCategory(year, category)
                .map(Standard::getTotal)
                .orElse(0);
    }

    private int getTotal(int total, Category category) {
        return switch (category) {
            case MAJOR_ADVANCED -> 0;
            case MAJOR_OPTIONAL -> 60;
            default -> total;
        };
    }
}
