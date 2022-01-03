package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements time = doc.select(".altCol");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
        }
        System.out.println("Выводим время по заданию");
        for (Element td: time) {
            Element href1 = td.parent();
            System.out.println(href1.text());
            System.out.println(href1.children().get(5).text());
        }
    }
}