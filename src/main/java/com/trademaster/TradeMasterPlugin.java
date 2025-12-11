package com.trademaster;

import com.google.inject.Provides;
import com.trademaster.controllers.HomeController;
import com.trademaster.db.DBManager;
import com.trademaster.models.HomeModel;
import com.trademaster.services.GEPriceService;
import com.trademaster.views.home.HomeView;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.ge.GrandExchangeTrade;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Trade Master",
        description = "Make GP the easy way! Provides advanced Grand Exchange tracking and item management."
)
public class TradeMasterPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private TradeMasterConfig config;

    @Inject
    private ItemManager itemManager;

    private NavigationButton navButton;
    private HomeModel model;
    private HomeController controller;


    @Override
    protected void startUp() throws Exception {
        log.debug("Example started!");

        model = new HomeModel();
        controller = new HomeController(model);
        HomeView view = new HomeView(controller);

        navButton = NavigationButton.builder()
                .tooltip("Trade Master")
                .icon(ImageUtil.loadImageResource(TradeMasterPlugin.class, "/icon.png"))
                .priority(2)
                .panel(view)
                .build();

        DBManager manager = new DBManager();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception {
        log.debug("Example stopped!");
        clientToolbar.removeNavigation(navButton);
    }

//    @Subscribe
//    public void onGameStateChanged(GameStateChanged gameStateChanged) {
//        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
//            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
//        }
//    }

    @Subscribe
    public void onGameTick(GameTick event) {
        // TODO: I dont want to do this onGameTick but every 60s
        ItemContainer invContainer = client.getItemContainer(InventoryID.INVENTORY);
        ItemContainer bankContainer = client.getItemContainer(InventoryID.BANK);
        GrandExchangeOffer[] geOffers = client.getGrandExchangeOffers();


        if (invContainer != null) {
            Item[] items = invContainer.getItems();
            long invWealth = 0;

            try {
                for (Item item : items) {
                    int itemId = item.getId();
                    int itemQuantity = item.getQuantity();
                    invWealth += (long) itemManager.getItemPrice(itemId) * itemQuantity; //TODO: what fucking price is this using ???
                }

                model.setInventoryWealth(invWealth);
                controller.refresh();
            } catch (Exception e) {
                log.warn("Failed to fetch GE price for inventory: {}", invWealth);
            }
        }

        if (bankContainer != null) {
            Item[] items = bankContainer.getItems();
            long bankWealth = 0;

            try {
                for (Item item : items) {
                    int itemId = item.getId();
                    int itemQuantity = item.getQuantity();
                    bankWealth += (long) itemManager.getItemPrice(itemId) * itemQuantity; //TODO: what fucking price is this using ???
                }

                model.setBankWealth(bankWealth);
                controller.refresh();
            } catch (Exception e) {
                log.warn("Failed to fetch GE price for bank: {}", bankWealth);
            }
        }


        long geWealth = 0;

        try {
            for (GrandExchangeOffer offer : geOffers) {
                int itemQuantity = offer.getTotalQuantity();
                int itemPrice = offer.getPrice();
                geWealth += (long) itemPrice * itemQuantity; //TODO: what fucking price is this using ???
            }

            model.setGeWealth(geWealth);
            controller.refresh();
        } catch (Exception e) {
            log.warn("Failed to fetch GE price for GE: {}", geWealth);
        }

    }


    @Provides
    TradeMasterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TradeMasterConfig.class);
    }
}
