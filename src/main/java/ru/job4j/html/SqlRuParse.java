package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
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
     * @param link ссылка на страницу
     * @return модель с данными.
     */

    List<Post> list(String link) throws IOException {
        List<Post> post = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        return post;
    }

    Post detail(String link) throws IOException {
        Post post = new Post();
        Document doc = Jsoup.connect(link).get();
        Elements header = doc.select(".messageHeader");
        Elements desc = doc.select(".msgBody");
        Elements postCreated = doc.select(".msgFooter");
        String href = doc.attr("href");
        return post;
    }

}