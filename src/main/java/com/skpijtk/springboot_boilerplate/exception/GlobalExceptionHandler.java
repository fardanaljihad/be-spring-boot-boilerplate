package com.skpijtk.springboot_boilerplate.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.skpijtk.springboot_boilerplate.dto.ErrorResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentialsException(
                BadCredentialsException ex, HttpServletRequest request) {

                log.warn("Bad credentials: {} on path {}", ex.getMessage(), request.getRequestURI());

                ErrorResponse errorResponse = new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage() + " / Invalid username or password",
                        "T-ERR-001",
                        request.getRequestURI(),
                        null
                );

                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

                log.warn("Access denied: {} on path {}", ex.getMessage(), request.getRequestURI());

                ErrorResponse errorResponse = new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        ex.getMessage() + ": Role Anda tidak memiliki izin untuk mengakses endpoint ini",
                        "T-ERR-011",
                        request.getRequestURI(),
                        null
                );

                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(
                MethodArgumentNotValidException ex, HttpServletRequest request) {

                int min_length = 6;
                int max_length = 12;
                String message = "";
                String messageId = "T-ERR-001";

                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        String fieldName = error.getField();
                        String defaultMessage = error.getDefaultMessage();
                        Object rejected = error.getRejectedValue();
                        int inputLength = rejected != null ? rejected.toString().length() : 0;

                        Object target = ex.getBindingResult().getTarget();
                        try {
                                Field field = target.getClass().getDeclaredField(fieldName);
                                Size size = field.getAnnotation(Size.class);
                                if (size != null) {
                                        min_length = size.min();
                                        max_length = size.max();
                                }
                        } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                        }

                        if (inputLength > max_length) {
                                message = fieldName + " must be at most " + max_length + " characters";
                                messageId = "T-ERR-003";
                        }
                        else if (inputLength < min_length) {
                                message = fieldName + " must be at least " + min_length + " characters";
                                messageId = "T-ERR-004";
                        }
                        else {
                                message = defaultMessage;
                        }

                }

                Map<String, String> fieldErrors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (existing, replacement) -> existing
                        ));

                List<String> details = fieldErrors.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .collect(Collectors.toList());

                log.warn("Validation error(s): {} on path {}", details, request.getRequestURI());

                ErrorResponse errorResponse = new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message,
                        messageId,
                        request.getRequestURI(),
                        details
                );

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }



        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex, HttpServletRequest request) {

                log.warn("Resource not found: {} on path {}", ex.getMessage(), request.getRequestURI());
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage(),
                                ex.getMessageId(),
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex, HttpServletRequest request) {
                log.warn("Illegal argument: {} on path {}", ex.getMessage(), request.getRequestURI());
                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ex.getMessage(),
                                ex.getMessageId(),
                                request.getRequestURI(),
                                null);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // @ExceptionHandler(MethodArgumentNotValidException.class)
        // public ResponseEntity<ErrorResponse> handleValidationExceptions(
        //                 MethodArgumentNotValidException ex, HttpServletRequest request) {

        //         List<String> details = ex.getBindingResult()
        //                         .getAllErrors()
        //                         .stream()
        //                         .map(error -> {
        //                                 String fieldName = (error instanceof FieldError)
        //                                                 ? ((FieldError) error).getField()
        //                                                 : error.getObjectName();
        //                                 String errorMessage = error.getDefaultMessage();
        //                                 return fieldName + ": " + errorMessage;
        //                         })
        //                         .collect(Collectors.toList());

        //         log.warn("Validation error(s): {} on path {}", details, request.getRequestURI());
        //         ErrorResponse errorResponse = new ErrorResponse(
        //                         LocalDateTime.now(),
        //                         HttpStatus.BAD_REQUEST.value(),
        //                         HttpStatus.BAD_REQUEST.getReasonPhrase(),
        //                         "Validation Failed",
        //                         request.getRequestURI(),
        //                         details);
        //         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        // }

        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ErrorResponse> handleAllExceptions(
        //                 Exception ex, HttpServletRequest request) {

        //         log.error("An unexpected error occurred: {} on path {}", ex.getMessage(), request.getRequestURI(), ex);

        //         ErrorResponse errorResponse = new ErrorResponse(
        //                         LocalDateTime.now(),
        //                         HttpStatus.INTERNAL_SERVER_ERROR.value(),
        //                         HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        //                         "An unexpected internal server error occurred.",
        //                         request.getRequestURI(),
        //                         null);
        //         return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        // }
}
