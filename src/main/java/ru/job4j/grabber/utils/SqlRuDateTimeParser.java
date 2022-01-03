package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries();

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse);
    }
}