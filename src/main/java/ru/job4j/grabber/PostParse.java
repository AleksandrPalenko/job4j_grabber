package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class PostParse {
    /**
     * Метод details загружает страницу сайта
     * @param link ссылка на страницу
     *"@return Post (с ссылкой, датой, названием, описанием)
     */

    public Post details(String link) throws IOException {
        Post post = new Post();
        Document doc = Jsoup.connect(link).get();
        post.setTitle(doc.select(".messageHeader").get(0).text());
        post.setDescription(doc.select(".content-wrapper-forum").get(0).text());
        post.setLink(link);
        SqlRuDateTimeParser dateTimeParser = new SqlRuDateTimeParser();
        String date = doc.select(".msgFooter").get(0).text().substring(0, 16);
        LocalDateTime localDateTime = dateTimeParser.parse(date);
        post.setCreated(localDateTime);
        return post;
    }

    public static void main(String[] args) throws IOException {
        PostParse parse = new PostParse();
        parse.details("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
    }
}
