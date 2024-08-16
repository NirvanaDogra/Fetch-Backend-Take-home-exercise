package com.nirvana.fetch_assessment.service;

import com.nirvana.fetch_assessment.dto.ReceiptInfo;

import java.text.ParseException;

public interface PointsService {
    int calculatePoints(ReceiptInfo receiptInfo) throws ParseException;
}