package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        String str = "https://www.sql.ru/forum/job-offers";
        String plusStr = str;
        for (int i = 0; i <= 5; i++) {
            plusStr = str + "/" + i;
        }
        DateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(plusStr).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.parent();
            System.out.println(href.child(1).child(0).attr("href"));
            System.out.println(href.child(1).text());
            System.out.println(dateTimeParser.parse(href.child(5).text()));
        }
    }
}