package com.trademaster.services.models;

import lombok.Getter;

@Getter
public class CachedPrice {
    private final GEItemPriceData price;
    private final long fetchedAt;

    public CachedPrice(GEItemPriceData price, long fetchedAt) {
        this.price = price;
        this.fetchedAt = fetchedAt;
    }
}
