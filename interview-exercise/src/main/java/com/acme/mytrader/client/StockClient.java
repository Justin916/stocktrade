package com.acme.mytrader.client;

import com.acme.mytrader.entity.Stock;

/**
 * There is no implementation for this interface however it is mocked in the test and the invocation is ensured
 */

public interface StockClient {

    Stock getLiveStockPrice(String security);

}
