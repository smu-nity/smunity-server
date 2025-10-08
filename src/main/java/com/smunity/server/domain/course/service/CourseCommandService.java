package com.smunity.server.domain.course.service;

import com.smunity.dto.AuthCourseResponseDto;
import com.smunity.dto.CourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequest;
import com.smunity.server.domain.auth.mapper.AuthMapper;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.course.dto.CourseResponse;
import com.smunity.server.domain.course.dto.ResultResponse;
import com.smunity.server.domain.course.entity.Course;
import com.smunity.server.domain.course.mapper.CourseMapper;
import com.smunity.server.domain.course.repository.course.CourseRepository;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.repository.MemberRepository;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.smunity.server.domain.course.service.StandardService.TOTAL_CREDITS;
import static com.smunity.server.global.common.logging.Loggers.event;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseCommandService {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final AuthMapper authMapper;
    private final CourseMapper courseMapper;

    public ResultResponse<CourseResponse> createCourses(Long memberId, AuthRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = toEntity(member, authService.readCourses(request));
        courseRepository.saveAll(courses);
        List<CourseResponse> responses = courseMapper.toResponse(member.getCourses());
        return courseMapper.toResponse(TOTAL_CREDITS, member.getCompletedCredits(), responses);
    }

    private List<Course> toEntity(Member member, AuthCourseResponseDto dto) {
        event.info("[CourseFetch] event=readCourses status=success memberId={} payload={}", member.getId(), dto);
        return dto.content().stream()
                .filter(c -> isValidCourse(member.getId(), c))
                .map(c -> toEntity(member, c))
                .toList();
    }

    private boolean isValidCourse(Long memberId, CourseResponseDto dto) {
        return !dto.grade().equals("F") && !courseRepository.existsByMemberIdAndNumber(memberId, dto.number());
    }

    private Course toEntity(Member member, CourseResponseDto dto) {
        Course course = authMapper.toEntity(dto, member.isDoubleMajor(), member.isNewCurriculum());
        course.setMember(member);
        return course;
    }
}
