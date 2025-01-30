package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.CreditResponseDto;
import com.smunity.server.domain.course.dto.CultureResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.course.entity.Curriculum;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.repository.CurriculumRepository;
import com.smunity.server.domain.course.repository.course.CourseRepository;
import com.smunity.server.global.common.entity.Member;
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

    private final StandardService standardService;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final CurriculumRepository curriculumRepository;

    public ResultResponseDto<CourseResponseDto> readCourses(Long memberId, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = courseRepository.findByMemberIdAndCategory(memberId, category);
        List<CourseResponseDto> responseDtoList = CourseResponseDto.from(courses);
        int total = standardService.getTotal(member.getYear(), member.getDepartment(), category);
        int completed = calculateCompleted(courses);
        return ResultResponseDto.of(total, completed, responseDtoList);
    }

    public CreditResponseDto readCoursesCredit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        int total = standardService.getTotal(member.getYear());
        return CreditResponseDto.from(total, member);
    }

    public ResultResponseDto<CultureResponseDto> readCultureCourses(Long memberId, Domain domain) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Curriculum> curriculums = curriculumRepository.findAllByYearAndDomain(member.getYear(), domain);
        List<CultureResponseDto> responseDtoList = CultureResponseDto.of(curriculums, member);
        int total = standardService.getCultureTotal(member.getExemption(), member.getDepartment(), curriculums.size(), domain);
        int completed = calculateCultureCompleted(responseDtoList);
        return ResultResponseDto.of(total, completed, responseDtoList);
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
