package com.trademaster.types;

import lombok.Getter;

@Getter
public enum TimestepType {
    FIVE_MINUTES("5m", "Last 5 minutes"),
    ONE_HOUR("1h", "Last 1 hour"),
    SIX_HOURS("6h", "Last 6 hours"),
    TWENTY_FOUR_HOURS("24h", "Last 24h");

    private final String timestepValue;
    private final String displayValue;
    // Timestep to return prices for.
    // If provided, will display 1-hour averages for all items we have data on for this time.
    // The timestamp field represents the beginning of the 1-hour period being averaged
//    private final long timestamp;

    TimestepType(String timestepValue, String displayValue) {
        this.timestepValue = timestepValue;
        this.displayValue = displayValue;
//        this.timestamp = timestamp;
    }
}
