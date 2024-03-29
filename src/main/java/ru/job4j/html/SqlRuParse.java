package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * Метод list загружает список постов
     * @param link ссылка на страницу
     *"@return список всех постов парсинга
     */

    /**
     * Метод detail Загружает все детали одного поста (имя, описание, дату создания, ссылку на пост).
     *
     * @param link ссылка на страницу
     * @return модель с данными.
     */

    public List<Post> list(String link) throws IOException, ParseException {
        /*в list нужно добавить цикл для парсинга 5 страниц и,
    используя ссылки на вакансии, парсить их в Post методом detail.
    */
        List<Post> postOfLIst = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect(link + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String linkForPost = href.attr("href");
                String text = href.text().toLowerCase(Locale.ROOT);
                if (text.contains("java")
                        && !text.contains("javaScript")) {
                    Post post = detail(linkForPost);
                    postOfLIst.add(post);
                }
            }
        }
        return postOfLIst;
    }

    public Post detail(String link) throws IOException, ParseException {
        Post post = new Post();
        Document doc = Jsoup.connect(link).get();
        post.setTitle(doc.select(".messageHeader").get(0).text());
        post.setDescription(doc.select(".msgBody").get(1).text());
        post.setLink(link);
        String date = doc.select(".msgFooter").get(0).text().substring(0, 16).trim();
        LocalDateTime localDateTime = dateTimeParser.parse(date);
        post.setCreated(localDateTime);
        return post;
    }

    public static void main(String[] args) throws IOException, ParseException {
        SqlRuDateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        SqlRuParse sqlRuParse = new SqlRuParse(dateTimeParser);
        System.out.println(sqlRuParse.detail("https://www.sql.ru/forum/1323839/razrabotchik-java-g-kazan"));
        System.out.println(sqlRuParse.list("https://www.sql.ru/forum/job-offers/"));
    }
}