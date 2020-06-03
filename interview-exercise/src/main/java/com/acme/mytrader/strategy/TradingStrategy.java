package com.acme.mytrader.strategy;

import com.acme.mytrader.entity.Order;
import com.acme.mytrader.entity.Stock;
import com.acme.mytrader.entity.constants.Direction;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceSource;
import com.acme.mytrader.price.impl.StockPriceUpdateObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 * <p>
 * 1. This class takes the order either buy or sell type when the trader needs to be place an order
 * 2. Ideally, the ExecutionService will be injected however for testing it is mocked with no implementation
 * 3. It also add the price listener with this instance - this is to cover the basis flow to ensure that order is created
 * 4. This will get an update when the price is changed, it will execute the filterOrderPredicate (this can be enhanced as of now no time)
 * 5. All the order that meet the condition will be executed based on the trade direction
 * <p>
 * Note: this flow covers only the price change flow, edge / exception case, already price is less than the
 * trigger price case are not covered
 */
public class TradingStrategy {

    private final ExecutionService executionService;
    private final PriceSource priceSource;
    List<Order> orderList = new ArrayList<Order>();
    StockPriceUpdateObserver stockPriceUpdateObserver = null;

    public TradingStrategy(PriceSource priceSource, ExecutionService executionService) {
        assert (Objects.nonNull(priceSource)) && (Objects.nonNull(executionService));
        this.priceSource = priceSource;
        this.stockPriceUpdateObserver = new StockPriceUpdateObserver(this.priceSource, this);
        this.executionService = executionService;
    }

    private static Predicate<Order> filterOrderPredicate(Stock liveStockPrice) {
        return o -> o.getStock().equals(liveStockPrice) && (liveStockPrice.getPrice().compareTo(o.getStock().getPrice()) < 0);
    }

    public void placeOrder(Order order) {
        assert (Objects.nonNull(order));
        this.orderList.add(order);
    }

    public void priceUpdate(Stock stock) {
        Predicate<Order> predicate = filterOrderPredicate(stock);
        List<Order> filteredOrder = this.orderList.stream().filter(predicate).collect(Collectors.toList());
        filteredOrder.forEach(o -> executeOrder(o));
    }

    private void executeOrder(Order order) {
        String security = order.getStock().getSecurity();
        Double triggerPrice = order.getStock().getPrice();
        Integer volume = order.getVolume();
        if (order.getDirection() == Direction.BUY) {
            this.executionService.buy(security, triggerPrice, volume);
        } else if (order.getDirection() == Direction.SELL) {
            this.executionService.sell(security, triggerPrice, volume);
        }
    }

}
