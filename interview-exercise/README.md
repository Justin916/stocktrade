# Developer Programming Exercise

## User Story

As a trader I want to be able to monitor stock prices such that when they breach a trigger level orders can be executed automatically.

## Exercise

Given the following interface definitions (provided)

```
public interface ExecutionService {
    void buy(String security, double price, int volume);
    void sell(String security, double price, int volume);
}
```

```
public interface PriceListener {
    void priceUpdate(String security, double price);
}
```

```
public interface PriceSource {
    void addPriceListener(PriceListener listener);
    void removePriceListener(PriceListener listener);
}
```

Develop a basic implementation of the PriceListener interface that provides the following behaviour:

1. Connects to a PriceSource instance
1. Monitors price movements on a specified single stock (e.g. "IBM")
1. Executes a single "buy" instruction for a specified number of lots (e.g. 100) as soon as the price of that stock is seen to be below
a specified price (e.g. 55.0). Don’t worry what units that is in.

### Considerations

* Please "work out loud" and ask questions
* This is not a test of your API knowledge so feel free to check the web as reference
* There is no specific solution we are looking for

### Some libraries already available:

* Java 8
* JUnit 4
* Mockito
* EasyMock
* JMock

## Solution / Development Flow:

1. As per the Domain-Driven Design, modelled the entities 'Stock' and 'Order' and followed the TDD to create unit test / entity / test data
2. StockFeedObservableTest and TradingStrategyTest are key to understand the end to end flow
3. In this flow, it tests the @PriceSource and @PriceListener to check the price change monitoring and price change update
4. In the  TradingStrategyTest, it tests the trader orders or executed as per the trade rule (order filer predicate based on the price change)
5. All the end to end flow is validated and  verified with less coverage 
6. Exception, edge case, quality and more refactoring should be done however due to timeline it is not taken care 
