package ru.job4j.grabber.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("март", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июнь", "6"),
            Map.entry("июль", "7"),
            Map.entry("авг", "8"),
            Map.entry("сент", "9"),
            Map.entry("окт", "10"),
            Map.entry("нояб", "11"),
            Map.entry("дек", "12")
    );

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
"20 дек 21, 19:48"
вчера, 13:19                                       parse = "20 дек 21, 19:48"
         */
        String[] data = parse.split(", "); /*  на 2 части "20 дек 21" и "19:48".*/
        String[] newData = data[0].split(" ");
        String[] time = data[1].split(":");
        LocalTime timeDate = LocalTime.of(Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
        String monthData = null;
        LocalDateTime rsl = null;
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

                rsl = LocalDateTime.of(Integer.parseInt(newData[2] + 2000
                        ), Integer.parseInt(monthData),
                        Integer.parseInt(newData[0]),
                        Integer.parseInt(time[0]),
                        Integer.parseInt(time[1]));
            }
        }
        return rsl;
    }

    public static void main(String[] args) throws IOException {
        /*
        Document dateInString = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = dateInString.select(".altCol");
         */
    }
}