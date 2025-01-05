package ru.iukr.linkshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.loggingstarter.LoggingStarterAutoConfiguration;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = LinkInfoProperty.class)
public class LinkShortenerApp {
    public static void main(String[] args) {
        LoggingStarterAutoConfiguration.println("Test");
        SpringApplication.run(LinkShortenerApp.class, args);
    }
}
