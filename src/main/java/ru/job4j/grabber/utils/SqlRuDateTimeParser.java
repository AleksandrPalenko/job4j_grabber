package ru.job4j.grabber.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("мар", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июн", "6"),
            Map.entry("июл", "7"),
            Map.entry("авг", "8"),
            Map.entry("сен", "9"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    @Override
    public LocalDateTime parse(String parse) throws IOException {

        String[] data = parse.split(", ");
        String[] newData = data[0].split(" ");
        String[] time = data[1].split(":");
        LocalTime timeDate = LocalTime.of(Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
        String monthData = null;
        LocalDateTime rsl;
        if ("сегодня".contains(data[0])) {
            rsl = LocalDateTime.of(LocalDate.now(),
                    timeDate
            );
        } else if ("вчера".contains(data[0])) {
            rsl = LocalDateTime.of(LocalDate.now().minusDays(1),
                    timeDate
            );
        } else {
            for (String map : MONTHS.keySet()) {
                if (newData[1].contains(map)) {
                    monthData = MONTHS.get(newData[1]);
                }
            }
            assert monthData != null;
            rsl = LocalDateTime.of(Integer.parseInt(newData[2]) + 2000,
                    Integer.parseInt(monthData),
                    Integer.parseInt(newData[0]),
                    Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]));
        }
        return rsl;
    }

    public static void main(String[] args) throws IOException {

    }
}