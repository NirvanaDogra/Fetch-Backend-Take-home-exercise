package com.nirvana.fetch_assessment.service.impl;

import com.nirvana.fetch_assessment.dto.PointsResponse;
import com.nirvana.fetch_assessment.dto.ReceiptCreationResponse;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import com.nirvana.fetch_assessment.exception.Error;
import com.nirvana.fetch_assessment.exception.FetchAssessmentException;
import com.nirvana.fetch_assessment.repository.FetchAssessmentRepository;
import com.nirvana.fetch_assessment.service.FetchAssessmentService;
import com.nirvana.fetch_assessment.service.PointsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.INVALID_RECEIPT;
import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.NOT_FOUND_RECEIPT;

@Log4j2
@Service
public class FetchAssessmentServiceImpl implements FetchAssessmentService {
    final FetchAssessmentRepository fetchAssessmentRepository;
    final PointsService pointsService;


    public FetchAssessmentServiceImpl(FetchAssessmentRepository fetchAssessmentRepository, PointsService pointsService) {
        this.fetchAssessmentRepository = fetchAssessmentRepository;
        this.pointsService = pointsService;
    }

    @Override
    public ReceiptCreationResponse processReceiptRequest(ReceiptInfo receiptInfo) {
        try {
            ReceiptInfo receiptInfoResponse = fetchAssessmentRepository.save(receiptInfo);
            return new ReceiptCreationResponse(receiptInfoResponse.getUuid());
        } catch (Exception exception) {
            throw getException(HttpStatus.BAD_REQUEST, INVALID_RECEIPT);
        }
    }

    @Override
    public PointsResponse getPoints(String requestId) {
        log.info("searching by {}", requestId);
        try {
            ReceiptInfo receiptInfo = fetchAssessmentRepository.findById(requestId).orElseThrow();
            return new PointsResponse(pointsService.calculatePoints(receiptInfo));
        } catch (Exception exception) {
            throw getException(HttpStatus.NOT_FOUND, NOT_FOUND_RECEIPT);
        }
    }

    FetchAssessmentException getException(HttpStatus status, String description) {
        return new FetchAssessmentException(status, List.of(new Error(com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.REQUEST_FAILED, description)));
    }

}
