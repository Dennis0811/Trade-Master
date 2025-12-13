package com.trademaster.controllers;

import com.trademaster.TradeMasterConfig;
import com.trademaster.models.HomeModel;
import com.trademaster.views.home.HomeView;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Slf4j
public class HomeController {
    @Inject
    private final TradeMasterConfig config;
    @Inject
    private final HomeModel model;
    @Setter
    private HomeView view;

    public HomeController(TradeMasterConfig config, HomeModel model) {
        this.config = config;
        this.model = model;
    }

    public void refresh() {
        updateWealthDisplay();
    }

    private void updateWealthDisplay() {
        long total = model.getPlayerWealth();
        long bank = model.getBankWealth();
        long inventory = model.getInventoryWealth();
        long ge = model.getGeWealth();

        String totalAbbreviated = abbreviateNumber(total);
        String totalFormatted = formatNumber(total);

        view.setWealthText(
                config.abbreviateHoverBankEnabled() ? abbreviateNumber(bank) : formatNumber(bank),
                config.abbreviateHoverInventoryEnabled() ? abbreviateNumber(inventory) : formatNumber(inventory),
                config.abbreviateHoverGeEnabled() ? abbreviateNumber(ge) : formatNumber(ge),
                config.abbreviateGpTotalEnabled() ? totalAbbreviated : totalFormatted,
                config.abbreviateHoverGpTotalEnabled() ? totalAbbreviated : totalFormatted
        );
    }


    public String formatNumber(long value) {
        return NumberFormat.getNumberInstance().format(value);
    }

    public String abbreviateNumber(long value) {
        final long minNumber = config.abbreviateThreshold().getValue();

        if (value < minNumber) {
            return formatNumber(value);
        }

        String[] suffixes = {"", "K", "M", "B", "T", "Qa", "Qi", "Se"};
        int suffixIndex = 0;
        double dividedValue = value;

        while (dividedValue >= 1000 && suffixIndex < suffixes.length - 1) {
            dividedValue /= 1000;
            suffixIndex++;
        }

        // Use DecimalFormat to remove trailing zeros, max 3 decimals
        DecimalFormat df = new DecimalFormat("#0.###");
        return df.format(dividedValue) + " " + suffixes[suffixIndex];
    }

}
