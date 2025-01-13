package ru.iukr.linkshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.dto.common.CommonListResponse;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LinkInfoControllerTest {
    @Autowired
    LinkInfoController linkInfoController;
    @Autowired
    LinkInfoRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String tomorrow = LocalDateTime.now().plusDays(1).toString();
    private static final String DESCRIPTION = "test";
    private static final String LINK = "https://github.com/";
    private static final String ID = "9aac5434-b5ad-47fd-9b32-d1b11fe6f079";


    private final CreateLinkInfoRequest body = CreateLinkInfoRequest.builder()
            .link("test")
            .active(true)
            .endTime(tomorrow)
            .build();

    @Test
    public void testCreateLinkInfo() {
        CommonRequest<CreateLinkInfoRequest> commonRequest = new CommonRequest<>(body);
        CommonResponse<LinkInfoResponse> response = linkInfoController.postCreateLinkInfo(commonRequest);
        assertEquals(response.getBody().getLink(), commonRequest.getBody().getLink());
    }

    @Test
    public void testUpdateLinkInfo() {
        CommonResponse<LinkInfoResponse> response = linkInfoController.postCreateLinkInfo(new CommonRequest<>(body));
        LinkInfoUpdateRequest updateRequest = LinkInfoUpdateRequest.builder()
                .id(String.valueOf(response.getBody().getId()))
                .link("test2")
                .build();
        CommonResponse<LinkInfoResponse> updateResponse = linkInfoController.postUpdateLinkInfo(new CommonRequest<>(updateRequest));
        assertEquals("test2", updateResponse.getBody().getLink());

    }

    @Test
    public void testDeleteLink() {
        CommonResponse<LinkInfoResponse> response = linkInfoController.postCreateLinkInfo(new CommonRequest<>(body));
        int numberOfLinks = repository.findAll().size();
        linkInfoController.deleteLinkInfo(response.getBody().getId());
        assertEquals(numberOfLinks - 1, repository.findAll().size());
    }

    @Test
    public void testFilterLink() {
        CommonResponse<LinkInfoResponse> response = linkInfoController.postCreateLinkInfo(new CommonRequest<>(body));
        CommonListResponse<LinkInfoResponse> listResponse = linkInfoController.filterLinkInfo(new CommonRequest<>(
                FilterLinkInfoRequest.builder()
                        .linkPart("test")
                        .build()
        ));
        assertFalse(listResponse.getBody().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("postCreateShortLinkInvalidRequestSource")
    void when_postCreateShortLink_withInvalidRequest_expect_validationError(CreateLinkInfoRequest createRequest,
                                                                            String validationErrorMessage,
                                                                            String field) throws Exception {
        CommonRequest<CreateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')].field").value(field))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')]").exists());
    }

    @ParameterizedTest
    @MethodSource("patchUpdateShortLinkInvalidRequestSource")
    void when_patchUpdateShortLink_withInvalidRequest_expect_validationError(LinkInfoUpdateRequest createRequest,
                                                                            String validationErrorMessage,
                                                                            String field) throws Exception {
        CommonRequest<LinkInfoUpdateRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(patch("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.errorMessage").value("Ошибка валидации"))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')].field").value(field))
                .andExpect(jsonPath("$.validationErrors[?(@.message == '" + validationErrorMessage + "')]").exists());
    }

    public static Stream<Arguments> postCreateShortLinkInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new CreateLinkInfoRequest(null, tomorrow, DESCRIPTION, true),
                        "Ссылка не может быть пустой", "body.link"),
                Arguments.of(new CreateLinkInfoRequest("", tomorrow, DESCRIPTION, true),
                        "Ссылка не может быть пустой", "body.link"),
                Arguments.of(new CreateLinkInfoRequest("wrong_url_pattern", tomorrow, DESCRIPTION, true),
                        "url не соответствует паттерну", "body.link"),
                Arguments.of(new CreateLinkInfoRequest(LINK, LocalDateTime.now().minusDays(1).toString(), DESCRIPTION, true),
                        "Дата окончания действия ссылки не верна", "body.endTime"),
                Arguments.of(new CreateLinkInfoRequest(LINK, "wrong_date_format", DESCRIPTION, true),
                        "Дата окончания действия ссылки не верна", "body.endTime"),
                Arguments.of(new CreateLinkInfoRequest(LINK, tomorrow, DESCRIPTION, null),
                        "Признак активности не может быть null", "body.active")
        );
    }

    public static Stream<Arguments> patchUpdateShortLinkInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new LinkInfoUpdateRequest(null, LINK, tomorrow, DESCRIPTION, true),
                        "Некорректный uuid", "body.id"),
                Arguments.of(new LinkInfoUpdateRequest("invalid_id", LINK, tomorrow, DESCRIPTION, true),
                        "Некорректный uuid", "body.id"),
                Arguments.of(new LinkInfoUpdateRequest(ID,"wrong_url_pattern", tomorrow, DESCRIPTION, true),
                        "url не соответствует паттерну", "body.link"),
                Arguments.of(new LinkInfoUpdateRequest(ID, LINK, LocalDateTime.now().minusDays(1).toString(), DESCRIPTION, true),
                        "Дата окончания действия ссылки не верна", "body.endTime"),
                Arguments.of(new LinkInfoUpdateRequest(ID, LINK, "wrong_date_format", DESCRIPTION, true),
                        "Дата окончания действия ссылки не верна", "body.endTime")
        );
    }
}
