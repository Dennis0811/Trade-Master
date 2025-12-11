package com.trademaster.db.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerData {
    @SerializedName("wealth")
    private WealthData wealthData;

    @SerializedName("geOffers")
    private TradeData[] currentGeOfferData;

    @SerializedName("geTrades")
    private TradeData[] pastTradeData;

    public PlayerData(WealthData wealthData, TradeData[] currentGeOfferData, TradeData[] pastTradeData) {
        this.wealthData = wealthData;
        this.currentGeOfferData = currentGeOfferData;
        this.pastTradeData = pastTradeData;
    }
}
