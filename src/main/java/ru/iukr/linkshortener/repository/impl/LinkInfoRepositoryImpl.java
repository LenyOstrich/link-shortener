package ru.iukr.linkshortener.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoUpdateModel;
import ru.iukr.linkshortener.repository.LinkInfoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepository {
    private final Map<String, LinkInfo> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(storage.get(shortLink));
    }

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());
        storage.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public List<LinkInfo> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public void deleteLink(UUID uuid) {
        List<Map.Entry<String, LinkInfo>> entryToRemove = new ArrayList<>();
        storage.entrySet().stream().filter(entry -> uuid == entry.getValue().getId()).findFirst().ifPresentOrElse(
                entryToRemove::add,
                () -> log.info("Не удалось найти сущность по id: {}", uuid)
        );
        entryToRemove.forEach(entry -> storage.remove(entry.getKey()));
    }

    @Override
    public LinkInfo update(LinkInfoUpdateModel linkInfo) {
        List<Map.Entry<String, LinkInfo>> entryToUpdate = new ArrayList<>();
        storage.entrySet().stream().filter(entry -> linkInfo.getId() == entry.getValue().getId()).findFirst().ifPresentOrElse(
                entryToUpdate::add,
                () -> log.info("Не удалось найти сущность по id: {}", linkInfo.getId())
        );
        entryToUpdate.forEach(entry -> {
            LinkInfo value = entry.getValue();
            if (linkInfo.getLink() != null) {
                value.setLink(linkInfo.getLink());
            }
            if (linkInfo.getEndTime() != null) {
                value.setEndTime(linkInfo.getEndTime());
            }
            if (linkInfo.getDescription() != null) {
                value.setDescription(linkInfo.getDescription());
            }
            if (linkInfo.getActive() != null) {
                value.setActive(linkInfo.getActive());
            }
        });
        return entryToUpdate.getFirst().getValue();
    }
}
