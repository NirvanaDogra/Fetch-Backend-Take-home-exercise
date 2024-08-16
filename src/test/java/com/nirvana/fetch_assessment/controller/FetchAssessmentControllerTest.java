package com.nirvana.fetch_assessment.controller;

import com.nirvana.fetch_assessment.dto.PointsResponse;
import com.nirvana.fetch_assessment.dto.ReceiptCreationResponse;
import com.nirvana.fetch_assessment.exception.Error;
import com.nirvana.fetch_assessment.exception.FetchAssessmentException;
import com.nirvana.fetch_assessment.service.FetchAssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FetchAssessmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FetchAssessmentService service;

    @Test
    public void processReceiptsSuccess() throws Exception {

        ReceiptCreationResponse response = new ReceiptCreationResponse("1123");
        String requestBody = getRequestBody();
        when(service.processReceiptRequest(any())).thenReturn(response);

        mockMvc.perform(post("/receipts/process").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()));

    }

    @Test
    public void processReceiptsError() throws Exception {
        String requestBody = getRequestBody();
        doThrow(getException(HttpStatus.BAD_REQUEST, INVALID_RECEIPT)).when(service).processReceiptRequest(any());

        mockMvc.perform(post("/receipts/process").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].title").value(REQUEST_FAILED))
                .andExpect(jsonPath("$.[0].description").value(INVALID_RECEIPT));
    }

    @Test
    public void testGetPointsSuccess() throws Exception {
        String requestId = "123";
        PointsResponse pointsResponse = new PointsResponse(1);
        when(service.getPoints(requestId)).thenReturn(pointsResponse);

        mockMvc.perform(get("/receipts/{requestId}/points", requestId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.points").value(pointsResponse.getPoints()));
    }

    @Test
    public void testGetPointsError() throws Exception {
        String requestId = "123";
        doThrow(getException(HttpStatus.NOT_FOUND, NOT_FOUND_RECEIPT)).when(service).getPoints(requestId);

        mockMvc.perform(get("/receipts/{requestId}/points", requestId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].title").value(REQUEST_FAILED))
                .andExpect(jsonPath("$.[0].description").value(NOT_FOUND_RECEIPT));
    }

    FetchAssessmentException getException(HttpStatus status, String description) {
        return new FetchAssessmentException(status, List.of(new Error(REQUEST_FAILED, description)));
    }

    private String getRequestBody() {
        return """
                {
                  "retailer": "M&M Corner Market",
                  "purchaseDate": "2022-03-20",
                  "purchaseTime": "14:33",
                  "items": [
                    {
                      "shortDescription": "Gatorade",
                      "price": "2.25"
                    },{
                      "shortDescription": "Gatorade",
                      "price": "2.25"
                    },{
                      "shortDescription": "Gatorade",
                      "price": "2.25"
                    },{
                      "shortDescription": "Gatorade",
                      "price": "2.25"
                    }
                  ],
                  "total": "9.00"
                }""";
    }
}