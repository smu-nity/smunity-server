package com.smunity.server.domain.course.service;

import com.smunity.server.domain.course.dto.CourseResponse;
import com.smunity.server.domain.course.dto.CreditResponse;
import com.smunity.server.domain.course.dto.CultureResponse;
import com.smunity.server.domain.course.dto.ResultResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.course.entity.enums.Domain;
import com.smunity.server.domain.course.mapper.CourseMapper;
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
    private final CurriculumService curriculumService;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    public ResultResponse<CourseResponse> readCourses(Long memberId, Category category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = courseRepository.findByMemberIdAndCategory(memberId, category);
        List<CourseResponse> responses = CourseMapper.INSTANCE.toResponse(courses);
        int total = standardService.getTotal(member.getYear(), member.getDepartment(), category);
        int completed = calculateCompleted(courses);
        return CourseMapper.INSTANCE.toResponse(total, completed, responses);
    }

    public CreditResponse readCoursesCredit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        int total = standardService.getTotal(member.getYear());
        return CourseMapper.INSTANCE.toResponse(total, member);
    }

    public ResultResponse<CultureResponse> readCultureCourses(Long memberId, Domain domain) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<CultureResponse> cultures = curriculumService.readCurriculums(member, domain);
        int total = standardService.getCultureTotal(member.getExemption(), member.getDepartment(), cultures.size(), domain);
        int completed = calculateCultureCompleted(cultures);
        return CourseMapper.INSTANCE.toResponse(total, completed, cultures);
    }

    private int calculateCompleted(List<Course> courses) {
        return courses.stream()
                .mapToInt(Course::getCredit)
                .sum();
    }

    private int calculateCultureCompleted(List<CultureResponse> cultures) {
        return cultures.stream()
                .filter(CultureResponse::completed)
                .toList()
                .size();
    }
}
