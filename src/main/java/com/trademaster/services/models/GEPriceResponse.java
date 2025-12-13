package com.trademaster.services.models;

import lombok.Getter;

import java.util.Map;

@Getter
public class GEPriceResponse {
    private Map<String, GEItemPriceData> data;
}
