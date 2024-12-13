package me.lloyd26.autorestart.utils;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    static Map<Character, TimeUnit> timeUnitMap = new HashMap<>();

    public static String convertToTimeString(String time) {
        int timeAmount = Integer.parseInt(time.substring(0, time.length() - 1));
        char timeUnit = time.charAt(time.length() - 1);
        String timeUnitName = getUnitFromChar(timeUnit).name();
        if (timeAmount == 1) timeUnitName = timeUnitName.substring(0, timeUnitName.length() - 1);
        return timeAmount + " " + timeUnitName.toLowerCase();
    }

    public static long convertToMillis(String time) {
        int timeAmount = Integer.parseInt(time.substring(0, time.length() - 1));
        char timeUnit = time.charAt(time.length() - 1);
        return getUnitFromChar(timeUnit).toMillis(timeAmount);
    }

    private static TimeUnit getUnitFromChar(char timeUnit) {
        if (!timeUnitMap.containsKey('h')) timeUnitMap.put('h', TimeUnit.HOURS);
        if (!timeUnitMap.containsKey('m')) timeUnitMap.put('m', TimeUnit.MINUTES);
        if (!timeUnitMap.containsKey('s')) timeUnitMap.put('s', TimeUnit.SECONDS);

        return timeUnitMap.get(timeUnit);
    }

    public static LocalTime parseTime(String timeStr, List<DateTimeFormatter> formattersList) {
        for (DateTimeFormatter formatter : formattersList) {
            try {
                return LocalTime.parse(timeStr, formatter);
            } catch (DateTimeException ignored) {}
        }

        return null;
    }

    public static void sortTimeAnnounceList(List<String> timeAnnounceList) {
        timeAnnounceList.sort((o1, o2) -> {
            Map<Character, Integer> timeUnitWeight = new HashMap<>();
            timeUnitWeight.put('h', 3);
            timeUnitWeight.put('m', 2);
            timeUnitWeight.put('s', 1);

            char unit1 = o1.charAt(o1.length() - 1);
            char unit2 = o2.charAt(o2.length() - 1);

            if (unit1 != unit2) {
                return timeUnitWeight.get(unit2) - timeUnitWeight.get(unit1);
            } else {
                int value1 = Integer.parseInt(o1.substring(0, o1.length() - 1));
                int value2 = Integer.parseInt(o2.substring(0, o2.length() - 1));
                return value2 - value1;
            }
        });
    }
}
