package com.example.clear_solutions_task.web;

import com.example.clear_solutions_task.dto.ResponseDto;
import com.example.clear_solutions_task.exception.InvalidUserDateInputException;
import com.example.clear_solutions_task.exception.UniqueUserException;
import com.example.clear_solutions_task.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                         HttpServletRequest request) {
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("An validation body error occurred for the request URI : {} , \n message : {}"
                , request.getRequestURI(), errorMessage);
        return ResponseEntity.badRequest()
                .body(new ResponseDto<>(exception.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(InvalidUserDateInputException.class)
    public ResponseEntity<ResponseDto<String>> handlerInvalidUserDateInputException(InvalidUserDateInputException exception,
                                                                            HttpServletRequest request) {
        log.error("An validation body error occurred for the request URI : {} , \n message : {}"
                , request.getRequestURI(), exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ResponseDto<>(exception.getMessage(),request.getRequestURI()));

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handlerUserNotFoundException(UserNotFoundException exception,
                                                               HttpServletRequest request) {
        log.error("An validation body error occurred for the request URI : {} , \n message : {}",
                request.getRequestURI(), exception.getMessage());
        return ResponseEntity.badRequest()
                .body(new ResponseDto<>(exception.getMessage(),request.getRequestURI()));
    }

    @ExceptionHandler(UniqueUserException.class)
    public ResponseEntity<ResponseDto<String>> handlerUniqueUserException(UniqueUserException exception,
                                                             HttpServletRequest request) {
        log.error("An validation body error occurred for the request URI : {} , \n message : {}",
                request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDto<>(exception.getMessage(),request.getRequestURI()));
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseDto<String>> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception,
                                                             HttpServletRequest request) {
        String errorMessage = "User with such email has been registered";
        log.error("An validation body error occurred for the request URI : {} , \n message : {}"
                , request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDto<>(errorMessage,request.getRequestURI()));
    }

}
