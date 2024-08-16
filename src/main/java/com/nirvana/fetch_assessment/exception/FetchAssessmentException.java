package com.nirvana.fetch_assessment.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@NoArgsConstructor

public class FetchAssessmentException extends RuntimeException {
    HttpStatus status;
    List<Error> errorList;

    public FetchAssessmentException(HttpStatus status, List<Error> errorList) {
        super("Fetch Assessment Exception");
        this.errorList = errorList;
        this.status = status;
    }
}
