package com.trademaster.db;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.trademaster.db.models.PlayerData;
import com.trademaster.db.models.TradeData;
import com.trademaster.db.models.WealthData;
import com.trademaster.types.OfferTypes;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class DbManager {
    private final String FOLDER_NAME = "trademaster";
    private final File DB_DIR = new File(RuneLite.RUNELITE_DIR, FOLDER_NAME);
    private final String playerName;

    private WealthData wealthData;
    private TradeData[] currentOfferData;
    private TradeData[] tradeData;


    public DbManager(String playerName, PlayerData playerData) {
        this.playerName = playerName;
        this.wealthData = playerData.getWealthData();
        this.currentOfferData = playerData.getCurrentGeOfferData();
        this.tradeData = playerData.getPastTradeData();
    }

    public DbManager(String playerName, WealthData wealthData) {
        this.playerName = playerName;
        this.wealthData = wealthData;
    }

    public DbManager(String playerName, TradeData[] tradeData, boolean isPastTradeData) {
        this.playerName = playerName;
        if (isPastTradeData) {
            this.tradeData = tradeData;

        } else {
            this.currentOfferData = tradeData;
        }
    }

    public void readFile() {
        // read file
    }

    public void writeToFile() {
        createFolder();

        JsonObject dbFile = new JsonObject();
        Gson gson = new Gson();

        // Wealth
        JsonObject wealth = createWealthJson();
        dbFile.add("wealth", wealth);

        // Current GE Offers
        JsonArray currentOffers = new JsonArray();
        JsonObject offer1 = createTradeJson();
        currentOffers.add(offer1);
        dbFile.add("currentGeOffers", currentOffers);

        // Past GE Trades
        JsonArray trades = new JsonArray();
        JsonObject trade1 = createTradeJson();
        trades.add(trade1);

        JsonObject trade2 = createTradeJson();
        trades.add(trade2);
        dbFile.add("geTrades", trades);

        Path newPath = DB_DIR.toPath().resolve(playerName + ".json");

        try {
            log.debug("DB is being saved! {}", newPath);
            Files.writeString(newPath, gson.toJson(dbFile));
        } catch (IOException e) {
            log.debug("Couldn't write to DB file! {}", e.getMessage());
        }
    }

    /**
     * Creates a Json Object fills it with data with WealthData.
     *
     * @return Returns the Json Object
     */
    private JsonObject createWealthJson() {
        JsonObject jo = new JsonObject();
        jo.addProperty("bank", wealthData.getBankWealth());
        jo.addProperty("inventory", wealthData.getInventoryWealth());
        jo.addProperty("ge", wealthData.getGeWealth());
        return jo;
    }

    /**
     * Creates a Json Object fills it with data with TradeData.
     *
     * @return Returns the Json Object
     */
    private JsonObject createTradeJson() {
        JsonObject jo = new JsonObject();
        jo.addProperty("itemId", 123);
        jo.addProperty("quantity", 123);
        jo.addProperty("price", 123);
        jo.addProperty("offerType", OfferTypes.BUY.name());
        jo.addProperty("startTime", 123);
        jo.addProperty("endTime", 123);
        return jo;
    }

    public void exportAsCsv() {
        // export a csv file created from my json file
    }

    /**
     * Creates a folder to house all data / db files.
     */
    public void createFolder() {
        if (!DB_DIR.exists()) {
            log.debug("DB directory is being created.");
            if (!DB_DIR.mkdir()) {
                log.debug("DB directory couldn't be created!");
            }
        } else {
            log.debug("DB directory already exists - {}", DB_DIR);
        }
    }

}
