package ru.iukr.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.iukr.loggingstarter.LoggingStarterAutoConfiguration;

@SpringBootApplication
public class LinkShortenerApp {
    public static void main(String[] args) {
        LoggingStarterAutoConfiguration.println("Test");
        SpringApplication.run(LinkShortenerApp.class, args);
    }
}
