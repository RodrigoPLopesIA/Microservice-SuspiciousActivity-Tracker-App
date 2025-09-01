package com.rodrigo.ms.suspicious_activity_tracker.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rodrigo.ms.suspicious_activity_tracker.dto.ErrorResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErroControllerDevice {
    


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), request.getRequestURI(), ex.getMessage(), null));
    } 

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult validation) {
        
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = validation.getFieldErrors();
        
        for(var data : fieldErrors){
            errors.put(data.getField(), data.getDefaultMessage());
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Invalid Argument", errors));
    } 
}
