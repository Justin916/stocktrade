package com.acme.mytrader.strategy;

import com.acme.mytrader.client.StockClient;
import com.acme.mytrader.entity.Order;
import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceSource;
import com.acme.mytrader.price.impl.StockFeedObservable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import static com.acme.mytrader.testdata.TestDataFactory.*;
import static org.mockito.Mockito.*;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 * <p>
 * 1. StockFeedObservableTest ensures that StockClient is invoked - MOCK
 * 2. StockFeedObservableTest ensured that PriceListener is invoked - MOCK
 * 3. This ensure that ExecutionService is invoked - MOCK
 * 4. Over all end to end flow is tested however there is a quality, betterment, reusability and all these must be done
 * 5. Since the limited time encores to go with this approach as the aim is to cover the end to end flow with MOCK
 * <p>
 * Note: Refer the TradingStrategy java doc to get some idea
 * <p>
 * 1. Lot of the test code is copied form StockFeedObservableTest and need to get clean code however to cover the basic flow with mock
 * others other items are not considered now
 */
@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    private VerificationMode oneTime;
    private VerificationMode zeroTime;
    private PriceSource priceSource;
    private Stock stockToMonitor;
    private Stock liveStockToVerify;
    private Order buyOrder;
    private Order sellOrder;
    private TradingStrategy tradingStrategy;
    @Mock
    private StockClient stockClient;
    @Mock
    private ExecutionService executionService;

    @Before
    public void setUp() {
        stockClient = mock(StockClient.class);
        stockToMonitor = createStock();
        liveStockToVerify = Stock.builder()
                .security(stockToMonitor.getSecurity())
                .price(50.00)
                .build();
        oneTime = Mockito.timeout(1200).times(1);
        buyOrder = createBuyOrder();
        sellOrder = createSellOrder();
        zeroTime = Mockito.timeout(1200).times(0);
        priceSource = StockFeedObservable.getPriceSource(stockClient, stockToMonitor);
        executionService = mock(ExecutionService.class);
        when(stockClient.getLiveStockPrice(stockToMonitor.getSecurity())).thenReturn(liveStockToVerify);
        tradingStrategy = new TradingStrategy(StockFeedObservable.getPriceSource(stockClient, stockToMonitor),
                executionService);
    }

    @After
    public void cleanUp() {
        stockClient = null;
        stockToMonitor = null;
        liveStockToVerify = null;
        executionService = null;
        tradingStrategy = null;
    }

    @Test
    public void createBuyOrderTestOk() {
        tradingStrategy.placeOrder(buyOrder);
        Stock orderStock = buyOrder.getStock();
        verify(executionService, oneTime).buy(orderStock.getSecurity(), orderStock.getPrice(), buyOrder.getVolume());
    }

    @Test
    public void createBuyOrderTestWithGreaterThanTriggerPriceOrderNotExecuted() {
        liveStockToVerify.setPrice(100.00);
        tradingStrategy.placeOrder(buyOrder);
        Stock orderStock = buyOrder.getStock();
        verify(executionService, zeroTime).buy(orderStock.getSecurity(), orderStock.getPrice(), buyOrder.getVolume());
    }

    @Test
    public void createBuyOrderTestWithEqualTriggerPriceOrderNotExecuted() {
        liveStockToVerify.setPrice(55.00);
        tradingStrategy.placeOrder(buyOrder);
        Stock orderStock = buyOrder.getStock();
        verify(executionService, zeroTime).buy(orderStock.getSecurity(), orderStock.getPrice(), buyOrder.getVolume());
    }

    @Test
    public void createSellOrderTestOk() {
        tradingStrategy.placeOrder(sellOrder);
        Stock orderStock = sellOrder.getStock();
        verify(executionService, oneTime).sell(orderStock.getSecurity(), orderStock.getPrice(), sellOrder.getVolume());
    }

    @Test(expected = Throwable.class)
    public void createBuyOrderTestWithExceptionResponse() {
        when(stockClient.getLiveStockPrice(stockToMonitor.getSecurity())).thenThrow(
                new Throwable("Not able to get live stock price"));
        tradingStrategy.placeOrder(buyOrder);
        Stock orderStock = buyOrder.getStock();
        verify(executionService, zeroTime).buy(orderStock.getSecurity(), orderStock.getPrice(), buyOrder.getVolume());
    }
}
