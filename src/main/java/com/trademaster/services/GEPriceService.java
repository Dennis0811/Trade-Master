package com.trademaster.services;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// TODO: make this file actually work so i dont have to rely on itemManager wiki prices


@Slf4j
public class GEPriceService {
    private static final String GE_API_BASE = "https://prices.runescape.wiki/api/v1/osrs/latest?id=";

    public static long getItemPrice(int itemId) throws IOException {
        URL url = new URL(GE_API_BASE + itemId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to fetch data: " + responseCode);
        }

        StringBuilder json = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            json.append(scanner.nextLine());
        }
        scanner.close();

        return parsePriceFromJSON(json.toString(), itemId);
    }

    private static long parsePriceFromJSON(String json, int itemId) {
        // Implement JSON parsing (e.g., using org.json or Gson)
        return 0;
    }
}
