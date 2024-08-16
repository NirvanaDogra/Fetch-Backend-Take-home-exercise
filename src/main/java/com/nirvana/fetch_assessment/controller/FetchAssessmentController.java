package com.nirvana.fetch_assessment.controller;

import com.nirvana.fetch_assessment.dto.PointsResponse;
import com.nirvana.fetch_assessment.dto.ReceiptCreationResponse;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import com.nirvana.fetch_assessment.exception.Error;
import com.nirvana.fetch_assessment.service.FetchAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.nirvana.fetch_assessment.constants.FetchAssessmentConstants.*;

@RestController
@RequestMapping(value = BASEURL)
@Tag(name = "Receipt Processor")
@Validated
public class FetchAssessmentController {
    final FetchAssessmentService fetchAssessmentService;

    public FetchAssessmentController(FetchAssessmentService fetchAssessmentService) {
        this.fetchAssessmentService = fetchAssessmentService;
    }

    @Operation(summary = "Submits a receipt for processing", description = "Submits a receipt for processing")
    @ApiResponse(responseCode = "200", description = "Returns the ID assigned to the receipt", content = {
            @Content(schema = @Schema(implementation = ReceiptCreationResponse.class))
    })
    @ApiResponse(responseCode = "400", description = "The receipt is invalid", content = {
            @Content(schema = @Schema(implementation = Error.class))
    })
    @PostMapping(POST_RECEIPT_DATA_ENDPOINT)
    ResponseEntity<ReceiptCreationResponse> processReceipts(@Valid @RequestBody ReceiptInfo receiptInfo) {
        return ResponseEntity.ok(fetchAssessmentService.processReceiptRequest(receiptInfo));
    }

    @Operation(summary = "Returns the points awarded for the receipt", description = "Returns the points awarded for the receipt")
    @ApiResponse(responseCode = "200", description = "The number of points awarded", content = {
            @Content(schema = @Schema(implementation = PointsResponse.class))
    })
    @ApiResponse(responseCode = "404", description = "No receipt found for that id", content = {
            @Content(schema = @Schema(implementation = Error.class))
    })
    @GetMapping(GET_POINTS_BY_ID_ENDPOINT)
    ResponseEntity<PointsResponse> getPoints(
            @Schema(description = "The number of points awarded", example = "adb6b560-0eef-42bc-9d16-df48f30e89b2")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(fetchAssessmentService.getPoints(id));
    }
}


