package com.acme.mytrader.entity;

import com.acme.mytrader.entity.constants.Direction;
import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Order {
    @NonNull
    private BigInteger orderNo;
    @NonNull
    private Stock stock;
    @NonNull
    private Integer volume;
    @NonNull
    private Direction direction;
}
