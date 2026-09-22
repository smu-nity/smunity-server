package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.Exemption;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardService {

    public static final int TOTAL_CREDITS = 130;

    public int getTotal(Member member, List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return TOTAL_CREDITS;
        }

        return categories.stream()
                .distinct()
                .mapToInt(category -> getTotal(member, category))
                .sum();
    }

    public int getCultureTotal(Exemption exemption, Department department, int size, Domain domain) {
        return exemption != Exemption.TRANSFER ? getCultureTotal(department, size, domain) : 0;
    }

    private int getTotal(Member member, Category category) {
        boolean isTransfer = member.isTransfer();
        boolean isNewCurriculum = member.getYear().isNewCurriculum();
        boolean isHasAdvanced = member.getDepartment().isHasAdvanced();

        return switch (category) {
            case MAJOR_ADVANCED -> isHasAdvanced && !isTransfer ? 15 : 0;
            case MAJOR_OPTIONAL -> isTransfer ? 60 : isHasAdvanced && !isNewCurriculum ? 45 : 60;
            case FIRST_MAJOR -> isNewCurriculum ? 45 : 36;
            case SECOND_MAJOR -> member.isSecondDeptComputerScience() ? 45 : 36;
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
