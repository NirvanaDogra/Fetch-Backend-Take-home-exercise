package com.nirvana.fetch_assessment.service.impl;

import com.nirvana.fetch_assessment.dto.Item;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import com.nirvana.fetch_assessment.exception.Error;
import com.nirvana.fetch_assessment.exception.FetchAssessmentException;
import com.nirvana.fetch_assessment.service.PointsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Character.isLetterOrDigit;

@Log4j2
@Component
public class PointsServiceImpl implements PointsService {
    private static final int POINTS_50 = 50;
    private static final int POINTS_25 = 25;
    private static final int POINTS_10 = 10;
    private static final int POINTS_6 = 6;
    private static final int POINTS_5 = 5;
    private static final int ZERO = 0;
    private static final int RETAILER_NAME_POINT_PER_CHAR = 1;
    private static final double TOTAL_MULTIPLE_OF = 0.25;
    private static final double ITEM_DESCRIPTION_MULTIPLICAND = 0.2;
    private static final LocalTime START_TIME = LocalTime.of(14, 0);
    private static final LocalTime END_TIME = LocalTime.of(16, 0);

    @Override
    public int calculatePoints(ReceiptInfo receiptInfo) throws FetchAssessmentException {
        log.info("Calculating points for receipt: {}", receiptInfo);
        try {
            int totalPoints = 0;

            totalPoints += calculateRetailerNamePoints(receiptInfo.getRetailer());
            totalPoints += calculateRoundDollarAmountPoints(receiptInfo.getTotal());
            totalPoints += calculateMultipleOfQuarterAmountPoints(receiptInfo.getTotal());
            totalPoints += calculateItemPairPoints(receiptInfo.getItems());
            totalPoints += calculateItemDescriptionPoints(receiptInfo.getItems());
            totalPoints += calculateOddDayPoints(receiptInfo.getPurchaseDate());
            totalPoints += calculateTimeOfDayPoints(receiptInfo.getPurchaseTime());

            log.info("Total points calculated: {}", totalPoints);
            return totalPoints;
        } catch (Exception e) {
            log.error("Error calculating points", e);
            throw new FetchAssessmentException(HttpStatus.BAD_REQUEST, List.of(
                    new Error("Error co", "points")
            ));
        }
    }

    private int calculateRetailerNamePoints(String retailer) {
        if (ObjectUtils.isEmpty(retailer)) return ZERO;

        int points = 0;
        char[] ar = retailer.toCharArray();
        for (char character : ar) {
            if (isLetterOrDigit(character)) {
                points += RETAILER_NAME_POINT_PER_CHAR;
            }
        }
        log.info("{} points - retailer name has {} characters", points, retailer.length());
        return points;
    }

    private int calculateRoundDollarAmountPoints(Double receiptTotal) {
        int points = receiptTotal.intValue() == receiptTotal ? POINTS_50 : ZERO;
        log.info("Round dollar amount points: {}", points);
        return points;
    }

    private int calculateMultipleOfQuarterAmountPoints(Double receiptTotal) {
        int points = receiptTotal % TOTAL_MULTIPLE_OF == ZERO ? POINTS_25 : ZERO;
        log.info("Multiple of quarter amount points: {}", points);
        return points;
    }

    private int calculateItemPairPoints(List<Item> items) {
        int points = ((int) items.size() / 2) * POINTS_5;
        log.info("Item pair points: {}", points);
        return points;
    }

    private int calculateItemDescriptionPoints(List<Item> items) {
        double totalDiscountedSum = items.stream()
                .filter(item -> item.getShortDescription().trim().length() % 3 == 0)
                .map(item -> Math.ceil(item.getPrice() * ITEM_DESCRIPTION_MULTIPLICAND))
                .reduce(0.0, Double::sum);
        log.info("{}  trimmed length of the item description", (int)totalDiscountedSum);
        return (int) totalDiscountedSum;
    }

    private int calculateOddDayPoints(Date purchaseDate) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(purchaseDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int points = (day % 2 != 0) ? POINTS_6 : ZERO;
        log.info("{} points - purchase day is odd", points);
        return points;
    }

    private int calculateTimeOfDayPoints(LocalTime purchaseTime) {
        int points = (purchaseTime.isAfter(START_TIME) && purchaseTime.isBefore(END_TIME)) ? POINTS_10 : ZERO;
        log.info("Time of day points: {}", points);
        return points;
    }
}