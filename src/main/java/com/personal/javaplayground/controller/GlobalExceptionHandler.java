package com.personal.javaplayground.controller;

import com.personal.javaplayground.exceptions.RecordExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMalFormated(MethodArgumentNotValidException methodArgumentNotValidException) {
        var errors = new HashMap<String, String>();
        errors.put("error", "Cannot validate the request, please check api-doc(v3/api-docs) for more information");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordExistsException.class)
    public ResponseEntity<Map<String, String>> handleRecordExists(RecordExistsException methodArgumentNotValidException) {
        var errors = new HashMap<String, String>();
        errors.put("error", methodArgumentNotValidException.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
