package com.trademaster.db.models;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WealthData {
    @SerializedName("bank")
    private long bankWealth;

    @SerializedName("inventory")
    private long inventoryWealth;

    @SerializedName("ge")
    private long geWealth;
}
