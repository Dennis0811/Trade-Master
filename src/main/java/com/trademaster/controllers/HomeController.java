package com.trademaster.controllers;

import com.trademaster.TradeMasterConfig;
import com.trademaster.models.HomeModel;
import com.trademaster.utils.NumberFormatUtils;
import com.trademaster.views.home.HomeView;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

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
        long minNumberForFormatting = config.abbreviateThreshold().getValue();

        String totalAbbreviated = abbreviateNumber(total, minNumberForFormatting);
        String totalFormatted = formatNumber(total);

        view.setWealthText(
                config.abbreviateHoverBankEnabled() ? abbreviateNumber(bank, minNumberForFormatting) : formatNumber(bank),
                config.abbreviateHoverInventoryEnabled() ? abbreviateNumber(inventory, minNumberForFormatting) : formatNumber(inventory),
                config.abbreviateHoverGeEnabled() ? abbreviateNumber(ge, minNumberForFormatting) : formatNumber(ge),
                config.abbreviateGpTotalEnabled() ? totalAbbreviated : totalFormatted,
                config.abbreviateHoverGpTotalEnabled() ? totalAbbreviated : totalFormatted
        );
    }

    private String abbreviateNumber(long number, long minNumber) {
        return NumberFormatUtils.abbreviateNumber(number, minNumber);
    }

    private String formatNumber(long number) {
        return NumberFormatUtils.formatNumber(number);
    }
}
