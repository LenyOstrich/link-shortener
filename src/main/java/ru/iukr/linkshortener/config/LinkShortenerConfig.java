package ru.iukr.linkshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class LinkShortenerConfig {

    @Bean
    public String notFoundPage() throws IOException {
        File file = ResourceUtils.getFile("classpath:templates/404.html");

        return Files.readString(file.toPath());
    }
}
