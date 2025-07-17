package com.test.hft.presentation.exception;

import com.test.hft.application.dto.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/**
 * Author: Vuxie
 * Date : 17/07/2025
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException ex, Locale locale) {
        String localizedMessage = messageSource.getMessage(
                ex.getErrorCode().getMessageKey(),
                ex.getArgs(),
                locale
        );

        log.warn("[BusinessException] code={} message={}", ex.getErrorCode().getCode(), localizedMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.fail(localizedMessage, ex.getErrorCode().getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGeneralException(Exception ex, Locale locale) {
        String localizedMessage = messageSource.getMessage(
                ErrorCode.INTERNAL_ERROR.getMessageKey(),
                null,
                locale
        );

        log.error("[UnhandledException] {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.fail(localizedMessage, ErrorCode.INTERNAL_ERROR.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, Locale locale) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return ResponseEntity
                .badRequest()
                .body(BaseResponse.fail("VALIDATION_ERROR", errorMessage));
    }
}
