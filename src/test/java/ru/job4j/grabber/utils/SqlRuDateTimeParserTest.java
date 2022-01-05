package ru.job4j.grabber.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {

    @Test
    public void whenDataFromOut() throws IOException {
        String data = "20 дек 21, 19:48";
        LocalDateTime expected = LocalDateTime.of(2020, 12, 21, 19, 48);
        LocalDateTime rsl = new SqlRuDateTimeParser().parse(data);
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenDataWithToday() throws IOException {
        String data = "сегодня, 13:19";
        LocalDateTime expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 19));
        LocalDateTime rsl = new SqlRuDateTimeParser().parse(data);
        assertThat(rsl, is(expected));
    }
}