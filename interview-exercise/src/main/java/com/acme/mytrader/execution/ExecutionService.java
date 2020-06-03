package com.acme.mytrader.execution;

/**
 * There is no implementation for this interface however it is mocked in the test and the invocation is ensured
 */

public interface ExecutionService {
    void buy(String security, double price, int volume);
    void sell(String security, double price, int volume);
}
