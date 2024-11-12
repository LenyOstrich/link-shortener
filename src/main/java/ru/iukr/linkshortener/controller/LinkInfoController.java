package ru.iukr.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.service.LinkInfoService;

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
                .body(linkInfoResponse)
                .build();
    }

}
