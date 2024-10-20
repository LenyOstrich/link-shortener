package ru.iukr.linkshortener.repository;

import ru.iukr.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.Optional;

public interface LinkInfoRepository {

    Optional<LinkInfo> findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();
}
