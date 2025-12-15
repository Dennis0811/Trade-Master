
package com.trademaster.services;

import com.trademaster.TradeMasterConfig;
import com.trademaster.services.models.GEItemPriceData;
import com.trademaster.utils.NumberFormatUtils;
import com.trademaster.utils.TimeUtils;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CustomTooltipService {
    @Inject
    private Client client;
    @Inject
    private TooltipManager tooltipManager;
    @Inject
    private ItemManager itemManager;
    @Inject
    private TradeMasterConfig config;
    @Inject
    private GEPriceService gePriceService;
    @Inject
    private ClientThread clientThread;

    private GEItemPriceData priceData;
    private int fallbackPrice;
    private int lastHoveredItemId;
    private int itemQuantity;
    private MenuEntry lastMenuEntry;


    public void handleOnBeforeRender() {
        if (client.isMenuOpen() || !config.geTooltipEnabled()) return;
        MenuEntry menuEntry = lastMenuEntry;

        if (menuEntry == null || !shouldEnableTooltip(menuEntry)) {
            lastHoveredItemId = -1;
            priceData = null;
            fallbackPrice = 0;
            return;
        }

        int itemId = menuEntry.getItemId();
        itemId = itemManager.canonicalize(itemId);
        if (itemId < 1) return;

        ItemComposition comp = itemManager.getItemComposition(itemId);
        if (comp.isTradeable() && priceData != null) {
            String tooltipText = buildGESection(priceData);
            if (tooltipText != null) {
                tooltipManager.add(new Tooltip(tooltipText));
            }
        }
        if (config.showSummarySection()) {
            String formattedTooltipString = buildSummarySection(priceData, fallbackPrice, comp);
            if (formattedTooltipString != null) {
                tooltipManager.add(new Tooltip(formattedTooltipString));
            }
        }
    }

    public void handleOnMenuEntryAdded(MenuEntryAdded menuEntryAdded) {
        if (!config.geTooltipEnabled()) return;

        MenuEntry menuEntry = menuEntryAdded.getMenuEntry();
        if (menuEntry == null) return;
        lastMenuEntry = menuEntry;

        int itemId = menuEntry.getItemId();
        int canonicalizedId = itemManager.canonicalize(itemId);

        if (lastHoveredItemId == canonicalizedId
                || canonicalizedId < 1
                || !shouldEnableTooltip(menuEntry))
            return;

        priceData = null;
        fallbackPrice = 0;
        ItemComposition comp = itemManager.getItemComposition(canonicalizedId);

        Widget widget = menuEntry.getWidget();
        if (widget == null) return;
        itemQuantity = widget.getItemQuantity();

        if (comp.isTradeable()) {
            CompletableFuture
                    .supplyAsync(() -> gePriceService.getPrice(canonicalizedId))
                    .thenAccept(data ->
                            clientThread.invokeLater(() -> {
                                if (lastHoveredItemId == canonicalizedId) {
                                    priceData = data;
                                }
                            })
                    );
        } else {
            fallbackPrice = gePriceService.getFallbackPrice(canonicalizedId);
        }

        lastHoveredItemId = canonicalizedId;
    }


    private String buildGESection(GEItemPriceData data) {
        StringBuilder formatString = new StringBuilder();

        int lastBuyPrice = data.getHigh();
        int lastSellPrice = data.getLow();
        long lastBuyTime = data.getHighTime();
        long lastSellTime = data.getLowTime();

        String lastBuyPriceString = NumberFormatUtils.formatNumber(lastBuyPrice);
        String lastSellPriceString = NumberFormatUtils.formatNumber(lastSellPrice);

        if (config.showLastBuyPrice()) {
            formatString.append("Last GE Buy Price: ")
                    .append(lastBuyPriceString)
                    .append(" GP</br>");
        }
        if (config.showLastSellPrice()) {
            formatString.append("Last GE Sell Price: ")
                    .append(lastSellPriceString)
                    .append(" GP</br>");
        }
        if (config.showLastBuyTime()) {
            formatString.append("Last GE Buy Time: ")
                    .append(TimeUtils.timeAgo(lastBuyTime))
                    .append("</br>");
        }
        if (config.showLastSellTime()) {
            formatString.append("Last GE Sell Time: ")
                    .append(TimeUtils.timeAgo(lastSellTime))
                    .append("</br>");
        }

        return ColorUtil.prependColorTag(formatString.toString(), Color.WHITE);
    }

    private String buildSummarySection(GEItemPriceData priceData, int fallbackPrice, ItemComposition comp) {
        int usedPrice = fallbackPrice;
        boolean itemIsTradeable = comp.isTradeable();

        if (priceData != null && itemIsTradeable) {
            usedPrice = priceData.getLow();
        }

        StringBuilder formatString = new StringBuilder();

        long priceQuantity = (long) usedPrice * itemQuantity;
        long haPriceQuantity = (long) comp.getHaPrice() * itemQuantity;
        int itemId = comp.getId();
        itemId = itemManager.canonicalize(itemId);

        if (itemId == ItemID.COINS || itemId == ItemID.PLATINUM) {
            formatString.append(NumberFormatUtils.formatNumber(priceQuantity))
                    .append(" GP</br>");
        } else if (priceQuantity > 0 && itemIsTradeable) {
            formatString.append("GE: ")
                    .append(NumberFormatUtils.formatNumber(priceQuantity))
                    .append(" GP");

            if (itemQuantity > 1) {
                formatString.append(" (")
                        .append(NumberFormatUtils.formatNumber(usedPrice))
                        .append(" ea)");
            }
            formatString.append("</br>");
        }

        if (config.showHaPrice() && haPriceQuantity > 0) {
            formatString.append("HA: ")
                    .append(NumberFormatUtils.formatNumber(haPriceQuantity))
                    .append(" GP");

            if (itemQuantity > 1) {
                formatString.append(" (")
                        .append(NumberFormatUtils.formatNumber(comp.getHaPrice()))
                        .append(" ea)");
            }
        }

        if (formatString.toString().isEmpty()) return null;
        return ColorUtil.prependColorTag(formatString.toString(), Color.WHITE);
    }

    private boolean shouldEnableTooltip(MenuEntry menuEntry) {
        int itemId = menuEntry.getItemId();
        itemId = itemManager.canonicalize(itemId);
        if (itemId < 1) return false;
        Widget widget = menuEntry.getWidget();
        return widget != null &&
                (WidgetInfo.INVENTORY.getId() == widget.getId()
                        || WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId() == widget.getId()
                        || WidgetInfo.BANK_ITEM_CONTAINER.getId() == widget.getId()
                        || WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER.getId() == widget.getId()
                );
    }


}
