package com.matjipdaehak.fo.exception;
import org.springframework.web.server.ResponseStatusException;

/**
 * 모든 애플리케이션상 예외를 표현하는 예외
 */
public class CustomException extends ResponseStatusException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(
                errorCode.getHttpStatus(),
                errorCode.getErrorMessage()
        );
        this.errorCode = errorCode;
    }
}
