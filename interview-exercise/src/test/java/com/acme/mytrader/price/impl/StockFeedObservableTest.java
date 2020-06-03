package com.acme.mytrader.price.impl;

import com.acme.mytrader.client.StockClient;
import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import static com.acme.mytrader.testdata.TestDataFactory.createStock;
import static org.mockito.Mockito.*;

/**
 * <pre>
 * This test is to ensure that StockFeedObservable (PriceSource implementation) is tested with basic coverage
 * to ensure that it updates the listener when the price of a specific security is changed
 *
 * StockFeedObservable is observed by StockPriceUpdateObserver (PriceListener implementation class)
 * Basically, it registers by seeking an update when the price of the security is changed
 * Currently, only one security is is being monitored "IBM" and this can be enhanced
 * Also, in order to  fetch the exchange price "StockClient" interface is used with Mock
 *
 * StockClient will provide the live security price when it is invoked, just for this flow the price monitor will check
 * the price is changed, if so, it will invoke the price listener that is registered (price listener is mocked)
 *
 * Note: Please note this class provides only basic coverage and there are much more quality and betterment need to be taken care,
 * as per the instruction not allowed to spend more than 90 to 100 min hence trying to do what is possible within the timeline
 *
 * </pre>
 */
@RunWith(MockitoJUnitRunner.class)
public class StockFeedObservableTest {

    private VerificationMode oneTime;
    private VerificationMode zeroTime;
    private PriceSource priceSource;
    private Stock stockToMonitor;
    private Stock liveStockToVerify;
    @Mock
    private StockClient stockClient;
    @Mock
    private PriceListener priceListener;

    @Before
    public void setUp() {
        stockToMonitor = createStock();
        liveStockToVerify = Stock.builder()
                .security(stockToMonitor.getSecurity())
                .price(54.00)
                .build();
        oneTime = Mockito.timeout(1000).times(1);
        zeroTime = Mockito.timeout(1000).times(0);
        stockClient = mock(StockClient.class);
        priceListener = mock(PriceListener.class);
    }

    @After
    public void cleanUp() {
        stockClient = null;
        stockToMonitor = null;
        liveStockToVerify = null;
        priceSource = null;
    }

    @Test
    public void testPriceMonitorChangeWithDifferentPrice() {
        when(stockClient.getLiveStockPrice(stockToMonitor.getSecurity())).thenReturn(liveStockToVerify);
        priceSource = StockFeedObservable.getPriceSource(stockClient, stockToMonitor);
        priceSource.addPriceListener(priceListener);
        verify(stockClient, oneTime).getLiveStockPrice(stockToMonitor.getSecurity());
        verify(priceListener, oneTime).priceUpdate(stockToMonitor.getSecurity(), liveStockToVerify.getPrice());
    }
}