package com.acme.mytrader.price.impl;

import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
import com.acme.mytrader.strategy.TradingStrategy;

import java.util.Objects;

/**
 * <pre>
 * This class is an Observer and hence it needs to be registered with Observable, as soon as it registers,
 * the price source starts the monitor the list of products and updates the price change, this class will delegate to the
 * TradingStrategy class which stores the orders the executes based on the condition
 *
 *
 * Note: The unit test for this class is not done by mocking the same in the Observable however end to end test will cover the actual test
 * Because of timeline, it is not done and the same is covered by the test strategy e2e
 *
 * </pre>
 */

public class StockPriceUpdateObserver implements PriceListener {

    private final PriceSource priceSource;
    private final TradingStrategy tradingStrategy;

    public StockPriceUpdateObserver(PriceSource priceSource, TradingStrategy tradingStrategy) {
        assert Objects.nonNull(priceSource) && Objects.nonNull(tradingStrategy);
        this.priceSource = priceSource;
        this.tradingStrategy = tradingStrategy;
        registerToPriceSource();
    }

    @Override
    public void priceUpdate(String security, double price) {
        tradingStrategy.priceUpdate(Stock.builder()
                .security(security)
                .price(price)
                .build());
    }

    private void registerToPriceSource() {
        this.priceSource.addPriceListener(this);
    }
}
