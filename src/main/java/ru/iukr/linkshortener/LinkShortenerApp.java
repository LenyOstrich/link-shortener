package ru.iukr.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.iukr.linkshortener.property.LinkInfoProperty;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = LinkInfoProperty.class)
public class LinkShortenerApp {
    public static void main(String[] args) {
        SpringApplication.run(LinkShortenerApp.class, args);
    }
}
