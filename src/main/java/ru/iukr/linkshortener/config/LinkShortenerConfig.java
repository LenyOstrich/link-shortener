package ru.iukr.linkshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.iukr.linkshortener.property.LinkInfoProperty;
import ru.iukr.linkshortener.repository.LinkInfoRepository;
import ru.iukr.linkshortener.service.LinkInfoService;
import ru.iukr.linkshortener.service.impl.LinkInfoServiceImpl;
import ru.iukr.linkshortener.service.impl.LogExecutionTimeLinkInfoServiceProxy;

@Configuration
public class LinkShortenerConfig {

    @Bean
    public LinkInfoService linkInfoService(LinkInfoRepository linkInfoRepository, LinkInfoProperty property) {
        LinkInfoService linkInfoService = new LinkInfoServiceImpl(linkInfoRepository, property);
        return new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
    }
}
