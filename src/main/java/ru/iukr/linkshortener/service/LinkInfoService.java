package ru.iukr.linkshortener.service;

import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;

import java.util.Map;

public interface LinkInfoService {

    public String generateLink(CreateLinkInfoRequest linkInfoRequest);
}
