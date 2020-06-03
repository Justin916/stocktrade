package com.acme.mytrader.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Stock {
    @NonNull
    private String security;
    @NonNull
    @EqualsAndHashCode.Exclude private Double price;
}
