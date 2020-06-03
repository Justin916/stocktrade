package com.acme.mytrader.price.impl;

import com.acme.mytrader.client.StockClient;
import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * This is the singleton Observable class and it privies the double lock check to ensure that not more than one instance is available.
 * It has ScheduledExecutorService, ScheduledFuture to fetch the price change by invoking the Stock client
 * Stock client will be mocked to provide the data and there is no implementation for the same
 * Thread safe is given the list collection however there is a better way of doing this using stamped lock however
 * the focus is to get the basis flow and approach hence it is not used
 * Note: Just to make this flow work for every 1 sec the polling will happen by invoking the StockClient
 *
 * When the first listener it registered, it will start the monitory to fetch the price change
 *
 *
 * </pre>
 */

public class StockFeedObservable implements PriceSource {

    private static volatile StockFeedObservable stockFeedReceiver = null;
    private final StockClient client;
    private final Stock stockToMonitor;
    private final ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> scheduledFuture = null;
    private List<PriceListener> priceChangeListeners = Collections.synchronizedList(new ArrayList<PriceListener>());
    private Integer period = 1;

    private StockFeedObservable(StockClient client, Stock stockToMonitor) {
        assert Objects.nonNull(client) && Objects.nonNull(stockToMonitor);
        this.client = client;
        this.stockToMonitor = stockToMonitor;
    }

    public static PriceSource getPriceSource(StockClient client, Stock stockToMonitor) {
        if (stockFeedReceiver == null) {
            synchronized (StockFeedObservable.class) {
                if (stockFeedReceiver == null) {
                    stockFeedReceiver = new StockFeedObservable(client, stockToMonitor);
                }
            }
        }
        return stockFeedReceiver;
    }

    @Override
    public void addPriceListener(PriceListener listener) {
        this.priceChangeListeners.add(listener);
        monitor();
    }

    @Override
    public void removePriceListener(PriceListener listener) {
        this.priceChangeListeners.remove(listener);
    }

    public void shutDown() {
        scheduledFuture.cancel(true);
        timerService.shutdown();
    }


    private void monitor() {
        scheduledFuture = timerService.scheduleAtFixedRate(this::getLivePrice, 0, period, TimeUnit.SECONDS);
    }

    private void getLivePrice() {
        String security = stockToMonitor.getSecurity();
        Double stockPrice = stockToMonitor.getPrice();
        Stock liveStock = this.client.getLiveStockPrice(security);
        assert Objects.nonNull(liveStock);
        Double liveStockPrice = liveStock.getPrice();
        if (liveStockPrice.compareTo(stockPrice) != 0) {
            notifyObservers(security, liveStockPrice);
            shutDown();
        }
    }

    private void notifyObservers(String security, Double liveStockPrice) {
        this.priceChangeListeners.forEach(priceListener -> notifyAndRemoveListener(security, liveStockPrice, priceListener));
    }

    private void notifyAndRemoveListener(String security, Double liveStockPrice, PriceListener priceListener) {
        priceListener.priceUpdate(security, liveStockPrice);
        this.priceChangeListeners.remove(priceListener);
    }

}
