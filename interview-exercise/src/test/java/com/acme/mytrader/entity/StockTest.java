package com.acme.mytrader.entity;

import org.junit.Assert;
import org.junit.Test;

import static com.acme.mytrader.testdata.TestDataFactory.*;
import static org.junit.Assert.assertEquals;

/**
 * <pre>
 * This test is to ensure that Stock entity is created and tested with more than 90% coverage,
 * this class gets test data from entity factory
 * </pre>
 */

public class StockTest {

    @Test
    public void createStockOk() {
        Assert.assertNotNull(createStock());
    }

    @Test
    public void createStockWithConstructorOk() {
        assertEquals(createStock(), createStockWithConstructor());
    }

    @Test(expected = NullPointerException.class)
    public void createStockWithInvalidStockName() {
        createStockWithoutStockName();
    }

    @Test(expected = NullPointerException.class)
    public void createStockWithInvalidPrice() {
        createStockWithoutPrice();
    }

}