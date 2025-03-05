package com.smunity.server.domain.course.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
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

@Service
@Transactional
@RequiredArgsConstructor
public class CourseCommandService {

    private final AuthService authService;
    private final StandardService standardService;
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    public ResultResponse<CourseResponse> createCourses(Long memberId, AuthRequest request) {
        List<AuthCourseResponseDto> responseDtos = authService.readCourses(request);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = responseDtos.stream()
                .filter(dto -> isValidCourse(memberId, dto))
                .map(dto -> toEntity(dto, member))
                .toList();
        courseRepository.saveAll(courses);
        List<CourseResponse> responses = CourseMapper.INSTANCE.toResponse(member.getCourses());
        int total = standardService.getTotal(member.getYear());
        return CourseMapper.INSTANCE.toResponse(total, member.getCompletedCredits(), responses);
    }

    private boolean isValidCourse(Long memberId, AuthCourseResponseDto dto) {
        return !dto.grade().equals("F") && !courseRepository.existsByMemberIdAndNumber(memberId, dto.number());
    }

    private Course toEntity(AuthCourseResponseDto dto, Member member) {
        Course course = AuthMapper.INSTANCE.toEntity(dto, member.isNewCurriculum());
        course.setMember(member);
        return course;
    }
}
