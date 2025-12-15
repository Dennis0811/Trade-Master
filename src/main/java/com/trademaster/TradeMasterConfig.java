package com.trademaster;

import com.trademaster.types.AbbreviateThresholdTypes;
import net.runelite.client.config.*;

@ConfigGroup("trademaster")
public interface TradeMasterConfig extends Config {
    @ConfigSection(
            position = 1,
            name = "Auto Save",
            description = "Automatically saves plugin data in whatever interval is set."
    )
    String autoSaveSection = "autoSaveSection";

    @ConfigItem(
            position = 1,
            section = autoSaveSection,
            keyName = "autoSaveEnabled",
            name = "Enable auto save",
            description = "Turns on/ off the auto saving feature."
    )
    default boolean autoSaveEnabled() {
        return true;
    }

    @ConfigItem(
            section = autoSaveSection,
            position = 2,
            keyName = "autoSaveInterval",
            name = "Auto Save Interval",
            description = "How often plugin data is saved (in minutes)."
    )
    @Units(Units.MINUTES)
    @Range(min = 1)
    default int autoSaveInterval() {
        return 5;
    }


    @ConfigSection(
            position = 2,
            name = "In-Game Tooltip Info",
            description = "Shows various information from the Grand Exchange in the tooltip when hovering over an item."
    )
    String tooltipInfo = "tooltipInfo";

    @ConfigItem(section = tooltipInfo,
            position = 1,
            keyName = "geTooltipEnabled",
            name = "Show Tooltip Information",
            description = "Displays additional tooltip information when hovering over an item."
    )
    default boolean geTooltipEnabled() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 2,
            keyName = "showLastBuyPrice",
            name = "Show Last Buy Price",
            description = "Displays the price at which the item was bought the last time."
    )
    default boolean showLastBuyPrice() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 3,
            keyName = "showLastSellPrice",
            name = "Show Last Sell Price",
            description = "Displays the price at which the item was sold the last time."
    )
    default boolean showLastSellPrice() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 4,
            keyName = "showLastBuyTime",
            name = "Show Last Buy Time",
            description = "Displays the time when the item was bought the last time."
    )
    default boolean showLastBuyTime() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 5,
            keyName = "showLastSellTime",
            name = "Show Last Sell Time",
            description = "Displays the time when the item was sold the last time."
    )
    default boolean showLastSellTime() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 6,
            keyName = "showSummarySection",
            name = "Show Summary Section",
            description = "Displays the HA and GE price multiplied with the item quantity."
    )
    default boolean showSummarySection() {
        return true;
    }

    @ConfigItem(
            section = tooltipInfo,
            position = 7,
            keyName = "stalePriceThreshold",
            name = "Stale Price Threshold",
            description = "Time in minutes before a price is considered old and the time is highlighted red."
    )
    @Units(Units.MINUTES)
    @Range(min = 1)
    default int stalePriceThreshold() {
        return 10;
    }


    @ConfigSection(
            name = "Sidebar Panel",
            description = "Settings for sidebar GP display.",
            position = 3
    )
    String shortenSection = "shortenSection";

    @ConfigItem(
            section = shortenSection,
            position = 1,
            keyName = "abbreviateGpTotalEnabled",
            name = "Abbreviate GP Total",
            description = "Abbreviates GP total value."
    )
    default boolean abbreviateGpTotalEnabled() {
        return true;
    }

    @ConfigItem(
            section = shortenSection,
            position = 2,
            keyName = "abbreviateThreshold",
            name = "Abbreviate Above",
            description = "Show abbreviated format (1.2 M) for numbers larger than this value."
    )
    default AbbreviateThresholdTypes abbreviateThreshold() {
        return AbbreviateThresholdTypes.TRILLION;
    }

    @ConfigItem(
            section = shortenSection,
            position = 3,
            keyName = "abbreviateHoverGpTotalEnabled",
            name = "Abbreviate Tooltip GP Total",
            description = "Abbreviates GP total value in tooltip."
    )
    default boolean abbreviateHoverGpTotalEnabled() {
        return false;
    }

    @ConfigItem(
            section = shortenSection,
            position = 4,
            keyName = "abbreviateHoverBankEnabled",
            name = "Abbreviate Tooltip Bank Value",
            description = "Abbreviates bank value in tooltip."
    )
    default boolean abbreviateHoverBankEnabled() {
        return false;
    }

    @ConfigItem(
            section = shortenSection,
            position = 5,
            keyName = "abbreviateHoverInventoryEnabled",
            name = "Abbreviate Tooltip Inventory Value",
            description = "Abbreviates inventory value in tooltip."
    )
    default boolean abbreviateHoverInventoryEnabled() {
        return false;
    }

    @ConfigItem(
            section = shortenSection,
            position = 6,
            keyName = "abbreviateHoverGeEnabled",
            name = "Abbreviate Tooltip GE Value",
            description = "Abbreviates Grand Exchange value in tooltip."
    )
    default boolean abbreviateHoverGeEnabled() {
        return false;
    }
}

