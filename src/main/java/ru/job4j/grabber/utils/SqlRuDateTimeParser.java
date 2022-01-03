package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse((CharSequence) MONTHS, FORMATTER);
    }

    public String time(String format) throws IOException, ParseException {
        Document dateInString = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = dateInString.select(".altCol");
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(format);
        return format;
    }
}