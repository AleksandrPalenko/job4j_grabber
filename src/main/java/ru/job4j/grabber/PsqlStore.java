package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;
import ru.job4j.quartz.AlertRabbit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Post postMethod(ResultSet rs) throws SQLException {
        return new Post(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("text"),
                rs.getString("link"),
                rs.getTimestamp("created").toLocalDateTime()
        );
    }

    @Override
    public void save(Post post) {
    /*
    сохраняет объявление в базе
     */
        try (PreparedStatement statement =
                     cnn.prepareStatement("Insert into post(name, text, link, created) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt("1"));
                }
            }
        } catch (SQLException th) {
            LOG.error(th.getMessage(), th);
        }
    }

    @Override
    public List<Post> getAll() {
        /*
        позволяет извлечь объявления из базы
         */
        List<Post> post = new ArrayList<>();
        try (PreparedStatement pr = cnn.prepareStatement("select * from post")) {
            try (ResultSet rs = pr.executeQuery()) {
                while (rs.next()) {
                    post.add(postMethod(rs));
                }
            }
        } catch (SQLException th) {
            LOG.error(th.getMessage(), th);
        }
        return post;
    }

    @Override
    public Post findById(int id) {
        /*
        позволяет извлечь объявление из базы по id
         */
        Post postId = null;
        try (PreparedStatement pr = cnn.prepareStatement("select * from post where id = ?")) {
            pr.setInt(1, id);
            try (ResultSet rs = pr.executeQuery()) {
                if (rs.next()) {
                    postId = postMethod(rs);
                }
            }
        } catch (SQLException th) {
            LOG.error(th.getMessage(), th);
        }
        return postId;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("post.properties")) {
            config.load(in);
        } catch (Exception th) {
            LOG.error(th.getMessage(), th);
        }
        PsqlStore ps = new PsqlStore(config);
        SqlRuParse parse = new SqlRuParse(new SqlRuDateTimeParser());
        String url = "https://www.sql.ru/forum/job-offers/";
        List<Post> post = parse.list(url);
        for (int i = 0; i <= 5; i++) {
            ps.save(post.get(i));
        }
    }
}