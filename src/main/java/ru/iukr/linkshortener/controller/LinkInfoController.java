package ru.iukr.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.service.LinkInfoService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/link-infos")
@RequiredArgsConstructor
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @PostMapping
    public CommonResponse<LinkInfoResponse> postCreateLinkInfo(@RequestBody CommonRequest<CreateLinkInfoRequest> request) {
        log.info("Поступил запрос на создание короткой ссылки: {}", request);

        LinkInfoResponse linkInfoResponse = linkInfoService.createLinkInfo(request.getBody());

        log.info("Короткая ссылка создана успешно: {}", linkInfoResponse);
        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    }

    @PatchMapping
    public CommonResponse<LinkInfoResponse> postUpdateLinkInfo(@RequestBody CommonRequest<LinkInfoUpdateRequest> request) {
        log.info("Поступил запрос на обновление короткой ссылки: {}", request);
        LinkInfoResponse linkInfoResponse = linkInfoService.updateLinkInfo(request.getBody());
        log.info("Короткая ссылка была обновлена: {}" , linkInfoResponse);
        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .body(linkInfoResponse)
                .build();
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse<?> deleteLinkInfo(@PathVariable  UUID id) {
        log.info("Поступил запрос на удаление короткой ссылки: {}", id);
        linkInfoService.deleteByLinkId(id);
        log.info("Короткая ссылка была удалена: {}", id);
        return CommonResponse.<LinkInfoResponse>builder()
                .id(UUID.randomUUID())
                .build();
    }

}
