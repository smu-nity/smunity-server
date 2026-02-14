package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.global.common.entity.Department;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.entity.enums.Exemption;
import org.springframework.stereotype.Service;

@Service
public class StandardService {

    public static final int TOTAL_CREDITS = 130;

    public int getTotal(Member member, Category category) {
        return category != null ?
                getTotal(member.getYear().isNewCurriculum(), member.getDepartment().isHasAdvanced(),
                        member.isSecondDeptComputerScience(), category) : TOTAL_CREDITS;
    }

    public int getCultureTotal(Exemption exemption, Department department, int size, Domain domain) {
        return exemption != Exemption.TRANSFER ? getCultureTotal(department, size, domain) : 0;
    }

    private int getTotal(boolean isNewCurriculum, boolean isHasAdvanced, boolean isComputerScience, Category category) {
        return switch (category) {
            case MAJOR_ADVANCED -> isHasAdvanced ? 15 : 0;
            case MAJOR_OPTIONAL -> isHasAdvanced && !isNewCurriculum ? 45 : 60;
            case FIRST_MAJOR -> isNewCurriculum ? 45 : 36;
            case SECOND_MAJOR -> isComputerScience ? 45 : 36;
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
