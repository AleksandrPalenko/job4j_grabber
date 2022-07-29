package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static java.util.Map.entry;

public class HhRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = Map.ofEntries(
            entry("января", "1"),
            entry("февраля", "2"),
            entry("марта", "3"),
            entry("апреля", "4"),
            entry("мая", "5"),
            entry("июня", "6"),
            entry("июля", "7"),
            entry("августа", "8"),
            entry("сентября", "9"),
            entry("октября", "10"),
            entry("ноября", "11"),
            entry("декабря", "12")
    );

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("d M y");

    @Override
    public LocalDateTime parse(String parse) throws ParseException {
        parse = String.join(" ", Arrays.copyOfRange(parse.split("\\s"), 2, 5));
        Date date = SIMPLE_DATE_FORMAT.parse(monthConvert(parse));
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static String monthConvert(String dateTime) {
        for (String str: MONTHS.keySet()) {
            if (dateTime.contains(str)) {
                dateTime = dateTime.replace(str, MONTHS.get(str));
                break;
            }
        }
        return dateTime;
    }

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://vladimir.hh.ru/vacancy/50745199?from=vacancy_search_list&query=Java&hhtmFrom=vacancy_search_list").get();
        String dateTime = doc.select(".vacancy-creation-time").get(0).text();
        HhRuDateTimeParser hhParser = new HhRuDateTimeParser();
        System.out.println(hhParser.parse(dateTime));
    }
}