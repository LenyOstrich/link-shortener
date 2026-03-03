package ru.iukr.linkshortener.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;
import ru.iukr.linkshortener.dto.FilterLinkInfoRequest;
import ru.iukr.linkshortener.dto.LinkInfoUpdateRequest;
import ru.iukr.linkshortener.dto.PageableRequest;
import ru.iukr.linkshortener.dto.SortRequest;
import ru.iukr.linkshortener.dto.common.CommonListResponse;
import ru.iukr.linkshortener.dto.common.CommonRequest;
import ru.iukr.linkshortener.dto.common.CommonResponse;
import ru.iukr.linkshortener.model.LinkInfo;
import ru.iukr.linkshortener.model.LinkInfoResponse;
import ru.iukr.linkshortener.repository.LinkInfoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LinkInfoControllerTest {
    @Autowired
    LinkInfoController linkInfoController;
    @Autowired
    LinkInfoRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
    private static final String DESCRIPTION = "test";
    private static final String LINK = "https://example.com";
    private static final String ID = "9aac5434-b5ad-47fd-9b32-d1b11fe6f079";
    private static final int SHORT_LINK_LENGTH = 8;
    public static final String LINK_INFOS_URL = "/api/v1/link-infos";


    @Test
    public void testCreateLinkInfo() throws Exception {
        CreateLinkInfoRequest body = CreateLinkInfoRequest.builder()
                .link(LINK)
                .active(true)
                .endTime(tomorrow)
                .build();
        CommonRequest<CreateLinkInfoRequest> commonRequest = new CommonRequest<>(body);
        MvcResult result = mockMvc.perform(post(LINK_INFOS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        CommonResponse<LinkInfoResponse> response = objectMapper.readValue(
                responseContent,
                new TypeReference<CommonResponse<LinkInfoResponse>>() {
                }
        );
        assertEquals(response.getBody().getLink(), commonRequest.getBody().getLink());
    }

    @Test
    public void testUpdateLinkInfo() throws Exception {
        LinkInfo linkInfo = getSavedLinkInfo(1);
        String oldName = linkInfo.getLink();
        LinkInfoUpdateRequest updateRequest = LinkInfoUpdateRequest.builder()
                .id(String.valueOf(linkInfo.getId()))
                .link(LINK)
                .build();
        CommonRequest<LinkInfoUpdateRequest> commonRequest = new CommonRequest<>(updateRequest);
        MvcResult result = mockMvc.perform(patch(LINK_INFOS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        CommonResponse<LinkInfoResponse> response = objectMapper.readValue(
                responseContent,
                new TypeReference<CommonResponse<LinkInfoResponse>>() {
                }
        );
        assertEquals(linkInfo.getLink(), response.getBody().getLink());
        assertNotEquals(oldName, response.getBody().getLink());
    }

    @Test
    public void testDeleteLink() throws Exception {
        LinkInfo linkInfo = getSavedLinkInfo(1);
        int initialCount = repository.findAll().size();
        linkInfoController.deleteLinkInfo(linkInfo.getId());
        mockMvc.perform(delete(LINK_INFOS_URL + "/delete/" + linkInfo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(initialCount - 1, repository.findAll().size());
    }

    @Test
    public void testFilterLink() throws Exception {
        createTestData();
        FilterLinkInfoRequest request = FilterLinkInfoRequest.builder()
                .linkPart("example")
                .page(PageableRequest.builder()
                        .number(1)
                        .size(5)
                        .sorts(List.of(new SortRequest("openingCount", "DESC")))
                        .build())
                .build();
        CommonRequest<FilterLinkInfoRequest> commonRequest = new CommonRequest<>(request);
        MvcResult result = mockMvc.perform(post(LINK_INFOS_URL + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        CommonListResponse<LinkInfoResponse> listResponse = objectMapper.readValue(
                responseContent,
                new TypeReference<CommonListResponse<LinkInfoResponse>>() {
                }
        );

        List<Long> actual = listResponse.getBody()
                .stream()
                .map(LinkInfoResponse::getOpeningCount)
                .toList();

        List<Long> expected = new ArrayList<>(actual);
        expected.sort(Comparator.reverseOrder());

        assertEquals(expected, actual);
        assertEquals(5, listResponse.getBody().size());
        assertFalse(listResponse.getBody().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("postCreateShortLinkInvalidRequestSource")
    void when_postCreateShortLink_withInvalidRequest_expect_validationError(CreateLinkInfoRequest createRequest,
                                                                            String validationErrorMessage,
                                                                            String field) throws Exception {
        CommonRequest<CreateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        mockMvc.perform(post(LINK_INFOS_URL)
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
                Arguments.of(new CreateLinkInfoRequest(LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true),
                        "Дата окончания действия ссылки не может быть в прошлом", "body.endTime"),
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
                Arguments.of(new LinkInfoUpdateRequest(ID, "wrong_url_pattern", tomorrow, DESCRIPTION, true),
                        "url не соответствует паттерну", "body.link"),
                Arguments.of(new LinkInfoUpdateRequest(ID, LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true),
                        "Нельзя проставить дату окончания действия ссылки в прошлом", "body.endTime")
        );
    }

    private void createTestData() {
        for (int i = 0; i < 10; i++) {
            final long openingCount = i;
            LinkInfo linkInfo = getSavedLinkInfo(i);
            repository.findById(linkInfo.getId())
                    .ifPresent(link -> {
                        link.setOpeningCount(openingCount);
                        repository.save(link);
                    });
        }
    }

    private @NotNull LinkInfo getSavedLinkInfo(int i) {
        LinkInfo linkInfo = LinkInfo.builder()
                .shortLink(RandomStringUtils.randomAlphanumeric(SHORT_LINK_LENGTH))
                .link("https://example" + i + ".com")
                .build();
        repository.save(linkInfo);
        return linkInfo;
    }
}
