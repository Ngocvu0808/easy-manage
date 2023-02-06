//package com.example.authservice.exception;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class CustomValidationException extends ResponseEntityExceptionHandler {
//
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String, Object> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put("message", errorMessage);
//            errors.put("timestamp", new Date(System.currentTimeMillis()));
//            errors.put("status", false);
//            errors.put("httpCode", status.value());
//            errors.put("field_error", fieldName);
//        });
//        return new ResponseEntity<>(errors, headers, HttpStatus.OK);
//    }
//}
