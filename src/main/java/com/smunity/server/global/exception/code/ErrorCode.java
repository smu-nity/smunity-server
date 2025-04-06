package com.smunity.server.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common Errors
    INTERNAL_SERVER_ERROR(500, "COMMON000", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(400, "COMMON001", "잘못된 요청입니다."),
    UNAUTHORIZED(401, "COMMON002", "로그인이 필요합니다."),
    METHOD_NOT_ALLOWED(405, "COMMON003", "지원하지 않는 Http Method 입니다."),
    FORBIDDEN(403, "COMMON004", "금지된 요청입니다."),
    INVALID_ENUM_VALUE(400, "COMMON005", "잘못된 요청 값입니다."),

    // Validation Errors
    VALIDATION_FAILED(400, "VALID001", "입력값에 대한 검증에 실패했습니다."),

    // Account Errors
    ACCOUNT_CONFLICT(409, "ACCOUNT001", "중복된 사용자 이름 입니다."),
    ACCOUNT_NOT_FOUND(404, "ACCOUNT002", "해당 사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(401, "ACCOUNT003", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN_EXCEPTION(401, "ACCOUNT004", "토큰이 올바르지 않습니다."),
    EXPIRED_JWT_EXCEPTION(401, "ACCOUNT005", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RELOGIN_EXCEPTION(401, "ACCOUNT006", "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_EXCEPTION(401, "ACCOUNT007", "로그인 후 이용가능합니다. 토큰을 입력해 주세요."),
    FORBIDDEN_EXCEPTION(403, "ACCOUNT008", "권한이 없습니다."),
    INVALID_REFRESH_TOKEN(401, "ACCOUNT009", "리프레시 토큰이 유효하지 않습니다. 다시 로그인 해주세요."),
    YEAR_NOT_FOUND(404, "ACCOUNT010", "해당 연도를 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(404, "ACCOUNT011", "해당 학과를 찾을 수 없습니다."),
    UNVERIFIED_USER(401, "ACCOUNT012", "인증되지 않은 사용자입니다."),

    // Member Errors
    MEMBER_FORBIDDEN(403, "MEMBER001", "사용자 권한이 없습니다."),
    MEMBER_NOT_FOUND(404, "MEMBER002", "해당 사용자가 없습니다."),
    MEMBER_NOT_EDITABLE(400, "MEMBER003", "수정할 수 없는 학과 입니다."),

    // Question Errors
    QUESTION_NOT_FOUND(404, "QUESTION001", "해당 질문을 찾을 수 없습니다."),

    // Answer Errors
    ANSWER_NOT_FOUND(404, "ANSWER001", "해당 답변을 찾을 수 없습니다."),

    // Term Errors
    TERM_NOT_FOUND(404, "TERM001", "해당 학기를 찾을 수 없습니다."),

    // Slack Errors
    SLACK_SERVER_ERROR(500, "SLACK000", "Slack 서버 에러, 관리자에게 문의 바랍니다.");

    private final int value;
    private final String code;
    private final String message;
}
