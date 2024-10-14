package ru.iukr.linkshortener.service.impl;

import org.junit.jupiter.api.Test;
import ru.iukr.linkshortener.dto.CreateLinkInfoRequest;

class LinkInfoServiceImplTest {

    @Test
    void test() {
        System.out.println(new LinkInfoServiceImpl().generateLink(new CreateLinkInfoRequest()));
    }
}