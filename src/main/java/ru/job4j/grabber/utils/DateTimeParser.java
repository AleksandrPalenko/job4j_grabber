package ru.job4j.grabber.utils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

public interface DateTimeParser {
    LocalDateTime parse(String parse) throws IOException, ParseException;
}