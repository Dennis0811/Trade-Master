package com.trademaster.services.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GEItemPriceData {
    private int high;
    private int low;
    private long highTime;
    private long lowTime;
}
