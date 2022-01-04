package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final String YESTERDAY = "вчера";
    private static final String TODAY = "сегодня";

    private static final Map<String, String> MONTHS = Map.ofEntries(Map.entry("янв", "1"), Map.entry("фев", "2"),
            Map.entry("март", "3"), Map.entry("апр", "4"), Map.entry("май", "5"), Map.entry("июнь", "6"),
            Map.entry("июль", "7"), Map.entry("авг", "8"), Map.entry("сент", "9"), Map.entry("окт", "10"),
            Map.entry("нояб", "11"), Map.entry("дек", "12"));

    /*
    нужно дату с сайта к примеру, такую "18 июн 21, 18:10" распарсить и перевести в LocalDateTime.
    Соответственно,есть Months и, когда проверки на вчера/сегодня не прошли, нужно вычленить из даты "июн"  ,
    затем, перебираете Months в поисках нужного месяца и заменяете его на число.

     */
    @Override
    public LocalDateTime parse(String parse) throws IOException {
        /*

     строку - разбить на дату и время. по ","
Дату проверить на вчера/сегодня, если проходит, использовать LocalDate.now или LocalDate.now().minusDays(1).
Если не прошло на вчера/сегодня, то дальше дату можете также split спарсить по пробел "  ", время по ":"
Если нет -  проверить, искать в мапе и меняете месяц. Не забыть из String в int парсить.
         */
        Document dateInString = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = dateInString.select(".altCol");
        String[] data = parse.split(",");
        String[] newData = parse.split(" ");
        if ("сегодня".contains(data[0])) {
            LocalDate.now();
        } else if ("вчера".contains(data[0])) {
            LocalDate.now().minusDays(1);
        } else {
            for (String map : MONTHS.keySet()) {
                if (newData[1].contains(map)) {
                    newData[1] = newData[1].replace(map, MONTHS.get(map));
                }
            }
        }
        return LocalDateTime.parse((CharSequence) MONTHS);
    }
}