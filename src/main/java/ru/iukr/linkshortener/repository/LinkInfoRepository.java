package ru.iukr.linkshortener.repository;

import ru.iukr.linkshortener.model.LinkInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository {

    Optional<LinkInfo> findByShortLink(String shortLink);

    LinkInfo save(LinkInfo linkInfo);

    List<LinkInfo> findAll();

    void deleteLink(UUID uuid);

    Optional<LinkInfo> findById(UUID id);
}
