package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse {

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

    List<Post> list(String link) throws IOException {
        /*в list нужно добавить цикл для парсинга 5 страниц и,
    используя ссылки на вакансии, парсить их в Post методом detail.
    */
        List<Post> post = new ArrayList<>();
        Post postLnk = new Post();
        for (int i = 1; i <= 5; i++) {
            DateTimeParser dateTimeParser = new SqlRuDateTimeParser();
            Document doc = Jsoup.connect(link + "/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.parent();
                if (postLnk.getTitle().contains("java")
                        || postLnk.getTitle().contains("Java")
                        || postLnk.getTitle().contains("JAVA")) {
                    post.add(detail(link));
                }
                System.out.println(href.child(1).child(0).attr("href"));
                System.out.println(href.child(1).text());
                System.out.println(dateTimeParser.parse(href.child(5).text()));
            }
        }
        return post;
    }

    Post detail(String link) throws IOException {
        Post post = new Post();
        Document doc = Jsoup.connect(link).get();
        post.setTitle(doc.select(".messageHeader").get(0).text());
        post.setDescription(doc.select(".msgBody").get(0).text());
        post.setLink(link);
        SqlRuDateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        String date = doc.select(".msgFooter").get(0).text().substring(0, 16);
        LocalDateTime localDateTime = dateTimeParser.parse(date);
        post.setCreated(localDateTime);
        return post;
    }

}