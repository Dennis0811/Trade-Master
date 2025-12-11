package com.trademaster.db.models;

import com.trademaster.types.OfferTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeData {
    private int itemId;
    private int quantity;
    private int price;
    private OfferTypes offerType;
    private long startTime;
    private long endTime;
}
