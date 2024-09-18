package com.smunity.server.domain.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseRequestDto(
        @NotBlank(message = "과목명 입력은 필수 입니다.")
        String name,
        @NotBlank(message = "학수번호 입력은 필수 입니다.")
        @Size(min = 8, max = 8, message = "학수번호는 8자리여야 합니다.")
        String number,
        @NotBlank(message = "연도 입력은 필수 입니다.")
        String year,
        @NotBlank(message = "학기 입력은 필수 입니다.")
        String semester,
        @NotBlank(message = "이수 구분 입력은 필수 입니다.")
        String type,
        String domain,
        @NotBlank(message = "학점 입력은 필수 입니다.")
        int credit
) {

}
