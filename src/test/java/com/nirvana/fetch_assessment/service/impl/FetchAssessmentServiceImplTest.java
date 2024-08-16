package com.nirvana.fetch_assessment.service.impl;

import com.nirvana.fetch_assessment.dto.PointsResponse;
import com.nirvana.fetch_assessment.dto.ReceiptCreationResponse;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import com.nirvana.fetch_assessment.exception.FetchAssessmentException;
import com.nirvana.fetch_assessment.repository.FetchAssessmentRepository;
import com.nirvana.fetch_assessment.service.FetchAssessmentService;
import com.nirvana.fetch_assessment.service.PointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.Optional;

import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.INVALID_RECEIPT;
import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.NOT_FOUND_RECEIPT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FetchAssessmentServiceImplTest {
    private FetchAssessmentService fetchAssessmentService;
    @Mock
    private FetchAssessmentRepository repo;
    @Mock
    private PointsService pointsService;

    @BeforeEach
    void setUp() {
        fetchAssessmentService = new FetchAssessmentServiceImpl(repo, pointsService);
    }

    @Test
    void processReceiptRequestSuccess() {
        ReceiptInfo realInfo = new ReceiptInfo();
        realInfo.setUuid("123");
        when(repo.save(realInfo)).thenReturn(realInfo);

        ReceiptCreationResponse result = fetchAssessmentService.processReceiptRequest(realInfo);

        assertEquals("123", result.getId());
    }

    @Test
    void processReceiptRequestError() {
        ReceiptInfo realInfo = new ReceiptInfo();
        realInfo.setUuid("123");
        doThrow(new RuntimeException("error")).when(repo).save(realInfo);
        try {
            fetchAssessmentService.processReceiptRequest(realInfo);
        } catch (FetchAssessmentException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            assertEquals(INVALID_RECEIPT, ex.getErrorList().get(0).getDescription());
        }
    }

    @Test
    void getPointsWithSuccess() throws ParseException {
        String requestId = "valid_id";
        ReceiptInfo receiptInfo = new ReceiptInfo();
        Integer points = 100;
        when(repo.findById(requestId)).thenReturn(Optional.of(receiptInfo));
        when(pointsService.calculatePoints(receiptInfo)).thenReturn(points);

        PointsResponse response = fetchAssessmentService.getPoints(requestId);

        assertEquals(points, response.getPoints());
    }

    @Test
    void getPointsWithRepoError() throws ParseException {
        String requestId = "valid_id";
        ReceiptInfo receiptInfo = new ReceiptInfo();
        Integer points = 100;
        doThrow(new RuntimeException("not found")).when(repo).findById(requestId);
        when(pointsService.calculatePoints(receiptInfo)).thenReturn(points);
        try {
            fetchAssessmentService.getPoints(requestId);
        } catch (FetchAssessmentException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals(NOT_FOUND_RECEIPT, exception.getErrorList().get(0).getDescription());
        }
    }

    @Test
    void getPointsWithPointsError() throws ParseException {
        String requestId = "valid_id";
        ReceiptInfo receiptInfo = new ReceiptInfo();
        when(repo.findById(requestId)).thenReturn(Optional.of(receiptInfo));
        doThrow(new FetchAssessmentException()).when(pointsService).calculatePoints(receiptInfo);
        try {
            fetchAssessmentService.getPoints(requestId);
        } catch (FetchAssessmentException exception) {
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals(NOT_FOUND_RECEIPT, exception.getErrorList().get(0).getDescription());
        }
    }
}