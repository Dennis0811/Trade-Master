package com.trademaster.db;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class DBManager {
    private final String FOLDER_NAME = "trademaster";
    private final File DB_DIR = new File(RuneLite.RUNELITE_DIR, FOLDER_NAME);
    private String playerName = "playerName";

    public DBManager(String playerName) {
        createFolder();
        this.playerName = playerName;
        writeToFile();
    }

    public void readFile() {
        // read file
    }

    public void writeToFile() {
        JsonObject dbFile = new JsonObject();
        Gson gson = new Gson();

        // Wealth
        JsonObject wealth = new JsonObject();
        wealth.addProperty("bank", 8461);
        wealth.addProperty("inventory", 5464);
        wealth.addProperty("ge", 5841);
        dbFile.add("wealth", wealth);

        // Current GE Offers
        JsonArray currentOffers = new JsonArray();
        JsonObject offer1 = new JsonObject();
        offer1.addProperty("startTime", 13518431L);
        offer1.addProperty("endTime", 93518431L);
        offer1.addProperty("price", 72);
        offer1.addProperty("offerType", "SELL");
        offer1.addProperty("quantity", 13);
        offer1.addProperty("itemId", 2313);
        currentOffers.add(offer1);
        dbFile.add("currentGeOffers", currentOffers);

        // GE Trades
        JsonArray trades = new JsonArray();
        JsonObject trade1 = new JsonObject();
        trade1.addProperty("startTime", 13518431L);
        trade1.addProperty("endTime", 93518431L);
        trade1.addProperty("price", 64);
        trade1.addProperty("offerType", "BUY");
        trade1.addProperty("quantity", 13);
        trade1.addProperty("itemId", 2313);
        trades.add(trade1);

        JsonObject trade2 = new JsonObject();
        trade2.addProperty("startTime", 93518431L);
        trade2.addProperty("endTime", 113518431L);
        trade2.addProperty("price", 32);
        trade2.addProperty("offerType", "BUY");
        trade2.addProperty("quantity", 26);
        trade2.addProperty("itemId", 2300);
        trades.add(trade2);
        dbFile.add("geTrades", trades);

        try {
            log.debug("Db file is being saved to " + DB_DIR);
            Files.writeString(DB_DIR.toPath().resolve(playerName + ".json"), gson.toJson(dbFile));
        } catch (IOException e) {
            log.debug("Couldn't write to DB file! " + e.getMessage());
        }
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
