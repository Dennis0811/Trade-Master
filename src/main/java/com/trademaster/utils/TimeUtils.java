package com.trademaster.utils;

public class TimeUtils {
    public static String timeAgo(long unixTime) {
        long now = System.currentTimeMillis();
        long diff = now - unixTime * 1000;

        if (diff < 0) {
            diff = 0;
        }

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) return seconds + " sec ago";
        if (minutes < 60) return minutes + " min ago";
        if (hours < 24) return hours + " hours ago";
        return days + " days ago";
    }

    /**
     * Checks if a given UNIX timestamp is older than a specified age in milliseconds.
     *
     * @param maxAgeMillis    The maximum allowed age in milliseconds.
     * @param unixTimeSeconds The UNIX timestamp in seconds.
     * @return true if the timestamp is older than maxAgeMillis, false otherwise.
     */
    public static boolean isOlderThan(long maxAgeMillis, long unixTimeSeconds) {
        long currentTimeMillis = System.currentTimeMillis();
        long timestampMillis = unixTimeSeconds * 1000L;

        long age = Math.max(0, currentTimeMillis - timestampMillis);

        return age > maxAgeMillis;
    }

    public static long minutesToMillis(int minutes) {
        return (long) minutes * 60000;
    }

}
