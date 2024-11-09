package com.smunity.server.domain.course.service;

import com.smunity.server.domain.auth.dto.AuthCourseResponseDto;
import com.smunity.server.domain.auth.dto.AuthRequestDto;
import com.smunity.server.domain.auth.service.AuthService;
import com.smunity.server.domain.course.dto.CourseResponseDto;
import com.smunity.server.domain.course.dto.ResultResponseDto;
import com.smunity.server.domain.course.entity.Course;
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
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;

    public ResultResponseDto<CourseResponseDto> createCourses(Long memberId, AuthRequestDto requestDto) {
        List<AuthCourseResponseDto> requestDtoList = authService.readCourses(requestDto);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));
        List<Course> courses = requestDtoList.stream()
                .filter(dto -> isValidCourse(memberId, dto))
                .map(dto -> toCourse(dto, member))
                .toList();
        courseRepository.saveAll(courses);
        List<CourseResponseDto> responseDtoList = CourseResponseDto.from(member.getCourses());
        return ResultResponseDto.of(member.getYear().getTotal(), member.getCompletedCredits(), responseDtoList);
    }

    private boolean isValidCourse(Long memberId, AuthCourseResponseDto dto) {
        return !dto.grade().equals("F") && !courseRepository.existsByMemberIdAndNumber(memberId, dto.number());
    }

    private Course toCourse(AuthCourseResponseDto dto, Member member) {
        Course course = dto.toEntity();
        course.setMember(member);
        return course;
    }
}
