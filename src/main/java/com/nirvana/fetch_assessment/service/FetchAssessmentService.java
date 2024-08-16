package com.nirvana.fetch_assessment.service;

import com.nirvana.fetch_assessment.dto.PointsResponse;
import com.nirvana.fetch_assessment.dto.ReceiptCreationResponse;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;

public interface FetchAssessmentService {
    ReceiptCreationResponse processReceiptRequest(ReceiptInfo receiptInfo);
    PointsResponse getPoints(String requestId);
}
