package com.acme.mytrader.entity;

import org.junit.Assert;
import org.junit.Test;

import static com.acme.mytrader.testdata.TestDataFactory.createBuyOrder;

/**
 * <pre>
 * This test is to ensure that Order entity is created and tested
 * </pre>
 */

public class OrderTest {

    @Test
    public void createOrderOk() {
        Assert.assertNotNull(createBuyOrder());
    }

}