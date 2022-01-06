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
        LocalDateTime expected = LocalDateTime.of(2021, 12, 20, 19, 48);
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

    @Test
    public void whenFullDateWithTime() throws IOException {
        String data = "25 июн 18, 21:56";
        LocalDateTime expected = LocalDateTime.of(2018, 6, 25, 21, 56);
        LocalDateTime rsl = new SqlRuDateTimeParser().parse(data);
        assertThat(rsl, is(expected));
    }

    @Test
    public void whenDataWithYesterday() throws IOException {
        String data = "вчера, 14:20";
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(14, 20));
        LocalDateTime rsl = new SqlRuDateTimeParser().parse(data);
        assertThat(rsl, is(expected));
    }
}