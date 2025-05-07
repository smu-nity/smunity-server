package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.Exemption;
import org.springframework.stereotype.Service;

@Service
public class StandardService {

    public static final int TOTAL_CREDITS = 130;

    public int getTotal(Year year, Department department, Category category) {
        return category != null ? getTotal(year.isNewCurriculum(), department.isHasAdvanced(), category) : TOTAL_CREDITS;
    }

    public int getCultureTotal(Exemption exemption, Department department, int size, Domain domain) {
        return exemption != Exemption.TRANSFER ? getCultureTotal(department, size, domain) : 0;
    }

    private int getTotal(boolean isNewCurriculum, boolean isHasAdvanced, Category category) {
        return switch (category) {
            case MAJOR_ADVANCED -> isHasAdvanced ? 15 : 0;
            case MAJOR_OPTIONAL -> isHasAdvanced && !isNewCurriculum ? 45 : 60;
            case CULTURE -> 33;
            default -> 0;
        };
    }

    private int getCultureTotal(Department department, int size, Domain domain) {
        return switch (domain) {
            case CORE -> 2;
            case BALANCE -> department.isHasAdvanced() ? 3 : 0;
            default -> size;
        };
    }
}
