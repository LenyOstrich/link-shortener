package ru.iukr.linkshortener.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {
    private final Map<String, LinkInfo> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(storage.get(shortLink))
                .filter(linkInfo -> linkInfo.getActive() && linkInfo.getEndTime().isAfter(LocalDateTime.now()));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        if (linkInfo.getId() == null) {
            linkInfo.setId(UUID.randomUUID());
        }
        storage.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public void deleteLink(UUID uuid) {
        storage.entrySet().stream()
                .filter(entry -> uuid.equals(entry.getValue().getId()))
                .findFirst()
                .ifPresentOrElse(
                        entry -> storage.remove(entry.getKey()),
                        () -> log.info("Не удалось найти сущность по id: {}", uuid)
                );
    }

    @Override
    public Optional<LinkInfo> findById(UUID id) {
        return storage.values().stream()
                .filter(linkInfo -> id.equals(linkInfo.getId()))
                .findFirst();
    }
}
