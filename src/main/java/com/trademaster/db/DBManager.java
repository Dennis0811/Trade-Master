package com.trademaster.db;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import java.io.File;

@Slf4j
public class DBManager {

    public DBManager() {
        createFolder("trademaster");
    }

    public void readFile() {
        // read file
    }

    public void writeToFile() {
        // check if folder exists
        // no -> create folder named trademaster
        // playerName.json exists?
        // no -> create file
        // modify file as needed
    }

    public void exportAsCsv() {
        // export a csv file created from my json file
    }


    /**
     * Creates a folder to house all data / db files.
     *
     * @param name Folder name
     */
    public void createFolder(String name) {
        final File DB_DIR = new File(RuneLite.RUNELITE_DIR, name);
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
