package ru.iukr.linkshortener.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.HashMap;

public class LinkInfoServiceImpl implements LinkInfoService {

    public HashMap<String, CreateLinkInfoRequest> linkMap = new HashMap<>();

    @Override
    public String generateLink(CreateLinkInfoRequest linkInfoRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(5);
        linkMap.put(shortLink, linkInfoRequest);
        return shortLink;
    }
}
