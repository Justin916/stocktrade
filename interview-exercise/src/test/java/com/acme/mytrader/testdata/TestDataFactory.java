package com.acme.mytrader.testdata;

import com.acme.mytrader.entity.Order;
import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.entity.constants.Direction;

import java.math.BigInteger;

/**
 * <pre>
 * This class provides entity / value objects to test, currently, supports for @Stock and @Order entities
 * </pre>
 */

public class TestDataFactory {

    private TestDataFactory() {

    }

    public static Stock createStock() {
        return Stock.builder()
                .security("IBM")
                .price(55.00)
                .build();
    }

    public static Stock createStockWithConstructor() {
        return new Stock("IBM", 100.00);
    }

    public static Stock createStockWithoutStockName() {
        return Stock.builder()
                .price(55.00)
                .build();
    }

    public static Stock createStockWithoutPrice() {
        return Stock.builder()
                .security("IBM")
                .build();
    }

    public static Order createBuyOrder() {
        return Order.builder()
                .orderNo(BigInteger.valueOf(1000))
                .volume(5)
                .stock(createStock())
                .direction(Direction.BUY)
                .build();
    }

    public static Order createSellOrder() {
        return Order.builder()
                .orderNo(BigInteger.valueOf(2000))
                .volume(3)
                .stock(createStock())
                .direction(Direction.SELL)
                .build();
    }

}
