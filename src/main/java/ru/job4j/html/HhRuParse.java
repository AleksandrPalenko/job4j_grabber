package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.GrabberForAnotherParser;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HhRuParse implements Parse {
    private final Properties cfg = new Properties();

    private final DateTimeParser dateTimeParser;

    public HhRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public void cfg() throws IOException {
        try (InputStream in = GrabberForAnotherParser.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        }
    }

    /* Test parsing hh.ru
    public static void main(String[] args) throws Exception {
        String link = "https://vladimir.hh.ru/search/vacancy?area=1&employment=full&schedule=remote"
                + "&search_field=name&clusters=true&no_magic=true&ored_clusters=true&items_on_page=20"
                + "&order_by=publication_time&enable_snippets=true&search_period=7&text=Java&hhtmFrom=vacancy_search_list&page=";
        Document document = Jsoup.connect(link).get();
        // Show title
        Elements rows = document.select(".vacancy-serp-item__layout");
        System.out.println(rows.size());
        rows.forEach(row -> {
            Element titleElement = row.select(".serp-item__name").first().select(".bloko-link").first();
            String linkElement = titleElement.select(".bloko-link").first().attr("href");
            String vacancyName = titleElement.text();
            String vacancyDetail = row.select(".vacancy-serp-item__info").text();
            System.out.println(vacancyName + " Link: " + linkElement);
            System.out.println("Details");
            System.out.println("------------------------------------------------------");
            System.out.println(vacancyDetail);
            String salary = row.select(".bloko-header-section-3").attr("data-qa", "vacancy-serp__vacancy-compensation").text();
            System.out.println("Salary: " + salary);
        });
    }
    */

    public List<Post> list(String link) throws IOException, ParseException {
        cfg();
        link = cfg.getProperty("parseUrl");
        System.out.println("Link: " + link);
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Document doc = Jsoup.connect(String.format(link + "%d", i)).get();
            Elements rows = doc.select(".vacancy-serp-item__layout");
            for (Element row : rows) {
                Element titleElement = row.select(".serp-item__name").first().select(".bloko-link").first();
                String vacancyLink = titleElement.select(".bloko-link").first().attr("href");
                String vacancyTitle = row.select(".bloko-header-section-3").attr("data-qa", "vacancy-serp__vacancy-compensation").text();
                String vacancyDetail = row.select(".vacancy-serp-item__info").select(".g-user-content").text();
                posts.add(new Post(vacancyTitle, vacancyLink, vacancyDetail, LocalDateTime.now()));
            }
        }
        return posts;
    }

}
