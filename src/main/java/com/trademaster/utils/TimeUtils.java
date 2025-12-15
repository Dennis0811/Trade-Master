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
}
