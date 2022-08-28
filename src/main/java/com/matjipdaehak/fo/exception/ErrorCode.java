package com.matjipdaehak.fo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * 401 Unauthorized - 인증 과정의 문제, 인증이 필요한경우
     */
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 Json Web Token으로 요청했습니다."),
    AUTHORIZATION_HEADER_ERROR(HttpStatus.UNAUTHORIZED, "요청의 Authorization헤더를 찾을 수 없거나 잘못된 형식입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /**
     * 422 Unprocessable Entity - 요청의 데이터가 비즈니스 로직을 처리하기에 문제가 있음.
     */
    LACK_OF_DATA(HttpStatus.UNPROCESSABLE_ENTITY, "필수정보가 누락된 요청입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생");

    private ErrorCode(HttpStatus httpStatus, String errorMessage){
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
