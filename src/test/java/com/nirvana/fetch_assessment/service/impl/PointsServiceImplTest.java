package com.nirvana.fetch_assessment.service.impl;

import com.nirvana.fetch_assessment.dto.Item;
import com.nirvana.fetch_assessment.dto.ReceiptInfo;
import com.nirvana.fetch_assessment.service.PointsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointsServiceImplTest {
    private PointsService pointsService;
    @BeforeEach
    void setUp() {
        pointsService = new PointsServiceImpl();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculatePointsTest1() throws ParseException {
        Integer points = pointsService.calculatePoints(getTest1());
        assertEquals(109, points);
    }

    @Test
    void calculatePointsTest2() throws ParseException {
        Integer points = pointsService.calculatePoints(getTest2());
        assertEquals(28, points);
    }

    // retail 0
    // 50 points if the total is round dollar amount
    // 25 points if the total is a multiple of 0.25.
    // item pair 0
    // multiply the price by 0.2 and round up to the nearest integer 0
    // odd date 6
    // time 0
    @Test
    void testPointsWhenRetailerIsEmpty() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setRetailer("");
        Integer points = pointsService.calculatePoints(test);
        assertEquals(81, points);
    }
    @Test
    void testPointsWhenRetailerHas2Char() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setRetailer("AB");
        Integer points = pointsService.calculatePoints(test);
        assertEquals(83, points);
    }
    @Test
    void testPointsWhenRetailerIsNull() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setRetailer(null);
        Integer points = pointsService.calculatePoints(test);
        assertEquals(81, points);
    }

    @Test
    void testPointsWhenRetailerHasSpecialCharacter() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setRetailer("M&b!#12");
        Integer points = pointsService.calculatePoints(test);
        assertEquals(85, points);
    }

    // retail 6
    // 50 points if the total is round dollar amount
    // 25 points if the total is a multiple of 0.25.
    // item pair 0
    // multiply the price by 0.2 and round up to the nearest integer 0
    // odd date 6
    // time 0
    @Test
    void getPointsWhenTotalIsARoundDollarAmountAndMultipleOf0_25() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setTotal(10.0);
        Integer points = pointsService.calculatePoints(test);
        assertEquals(87, points);
    }

    @Test
    void getPointsWhenTotalIsNotARoundDollarAmountAndNotAMultipleOf0_25() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setTotal(10.12);
        Integer points = pointsService.calculatePoints(test);
        assertEquals(12, points);
    }

    // retail 6
    // 50 points if the total is round dollar amount
    // 25 points if the total is a multiple of 0.25.
    // item pair 10
    // multiply the price by 0.2 and round up to the nearest integer 0
    // odd date 6
    // time 0
    @Test
    void getPointsWhenForFourItems() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setItems(List.of(
                new Item("1", "Gatorade", 2.25D),
                new Item("1", "Gatorade", 2.25D),
                new Item("1", "Gatorade", 2.25D),
                new Item("1", "Gatorade", 2.25D)
        ));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(97, points);
    }

    @Test
    void getPointsWhenTimeIsNotBetween2and3() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setPurchaseTime(LocalTime.parse("13:59"));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(87, points);
    }

    // retail 6
    // 50 points if the total is round dollar amount
    // 25 points if the total is a multiple of 0.25.
    // item pair 10
    // multiply the price by 0.2 and round up to the nearest integer 0
    // odd date 6
    // time 10
    @Test
    void getPointsWhenTimeIs2() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setPurchaseTime(LocalTime.parse("14:01"));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(97, points);
    }

    @Test
    void getPointsWhenTimeIs3() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setPurchaseTime(LocalTime.parse("15:00"));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(97, points);
    }

    @Test
    void getPointsWhenTimeIs4() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setPurchaseTime(LocalTime.parse("16:00"));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(87, points);
    }

    @Test
    void getPointsWhenTimeMoreThan4() throws ParseException {
        ReceiptInfo test = getBasicTest();
        test.setPurchaseTime(LocalTime.parse("16:01"));
        Integer points = pointsService.calculatePoints(test);
        assertEquals(87, points);
    }


    ReceiptInfo getBasicTest() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2022-01-01");
        LocalTime localTime = LocalTime.parse("13:01");
        return new ReceiptInfo("1", "Target", date, localTime, List.of(), 0D);
    }
    ReceiptInfo getTest1() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2022-03-20");
        LocalTime localTime = LocalTime.parse("14:31");
        return new ReceiptInfo(
                "1",
                "M&M Corner Market",
                date,
                localTime,
                List.of(
                        new Item("1", "Gatorade", 2.25D),
                        new Item("1", "Gatorade", 2.25D),
                        new Item("1", "Gatorade", 2.25D),
                        new Item("1", "Gatorade", 2.25D)
                ),
                9.00D
        );
    }
    ReceiptInfo getTest2() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2022-01-01");
        LocalTime localTime = LocalTime.parse("13:01");
        return new ReceiptInfo(
                "1",
                "Target",
                date,
                localTime,
                List.of(
                        new Item("1", "Mountain Dew 12PK", 6.49D),
                        new Item("1", "Emils Cheese Pizza", 12.25D),
                        new Item("1", "Knorr Creamy Chicken", 1.26D),
                        new Item("1", "Doritos Nacho Cheese", 3.35D),
                        new Item("1", "   Klarbrunn 12-PK 12 FL OZ  ", 12.00D)
                ),
                35.35D
        );
    }
}