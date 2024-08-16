package com.nirvana.fetch_assessment.controller.advise;

import com.nirvana.fetch_assessment.exception.Error;
import com.nirvana.fetch_assessment.exception.FetchAssessmentException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log4j2
public class FetchAssessmentAdvice {
    @ExceptionHandler({ConstraintViolationException.class})
    private ResponseEntity<List<Error>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<Error> errorList = new ArrayList<>();
        for(val it: exception.getConstraintViolations()) {
            Error error = new Error(it.getPropertyPath().toString(), it.getMessage());
            errorList.add(error);
        }
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class, HttpMessageNotReadableException.class})
    private ResponseEntity<List<Error>> handleHttpMediaTypeNotAcceptableException(Exception ex) {
        List<Error> errorList  = List.of(new Error("",ex.getMessage()));
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler({FetchAssessmentException.class})
    private ResponseEntity<List<Error>> handleFetchAssessmentException(FetchAssessmentException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorList());
    }
}
