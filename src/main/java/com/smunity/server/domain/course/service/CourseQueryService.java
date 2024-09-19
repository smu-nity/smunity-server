package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.CreditResponseDto;
import com.smunity.server.domain.course.dto.CultureResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.domain.course.entity.Standard;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.repository.CurriculumRepository;
import com.smunity.server.domain.course.repository.StandardRepository;
import com.smunity.server.domain.course.repository.course.CourseRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.Year;
import com.smunity.server.global.common.entity.enums.Category;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseQueryService {

    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final StandardRepository standardRepository;
    private final CurriculumRepository curriculumRepository;

    public ResultResponseDto<CourseResponseDto> getCourses(Long memberId, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = courseRepository.findByMemberIdAndCategory(memberId, category);
        List<CourseResponseDto> responseDtoList = CourseResponseDto.from(courses);
        int total = getTotal(member.getYear(), category);
        int completed = calculateCompleted(courses);
        return ResultResponseDto.of(total, completed, responseDtoList);
    }

    public CreditResponseDto getCoursesCredit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        return CreditResponseDto.from(member);
    }

    public ResultResponseDto<CultureResponseDto> getCultureCourses(Long memberId, Domain domain) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Curriculum> curriculums = curriculumRepository.findAllByYearAndDomain(member.getYear(), domain);
        List<CultureResponseDto> responseDtoList = CultureResponseDto.of(curriculums, member);
        int total = getCultureTotal(curriculums.size(), domain);
        int completed = calculateCultureCompleted(responseDtoList);
        return ResultResponseDto.of(total, completed, responseDtoList);
    }

    private int getTotal(Year year, Category category) {
        return standardRepository.findByYearAndCategory(year, category)
                .map(Standard::getTotal)
                .orElseGet(year::getTotal);
    }

    private int getCultureTotal(int size, Domain domain) {
        return switch (domain) {
            case CORE -> 2;
            case BALANCE -> 3;
            default -> size;
        };
    }

    private int calculateCompleted(List<Course> courses) {
        return courses.stream()
                .mapToInt(Course::getCredit)
                .sum();
    }

    private int calculateCultureCompleted(List<CultureResponseDto> cultures) {
        return cultures.stream()
                .filter(CultureResponseDto::completed)
                .toList()
                .size();
    }
}
