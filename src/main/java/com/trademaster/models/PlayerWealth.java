package com.trademaster.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerWealth {
    private String bank;
    private String inventory;
    private String ge;
    private String total;
    private String totalAbbreviated;

    public PlayerWealth(String bank, String inventory, String ge, String total, String totalAbbreviated) {
        this.bank = bank + " GP";
        this.inventory = inventory + " GP";
        this.ge = ge + " GP";
        this.total = total + " GP";
        this.totalAbbreviated = totalAbbreviated + " GP";
    }
}
