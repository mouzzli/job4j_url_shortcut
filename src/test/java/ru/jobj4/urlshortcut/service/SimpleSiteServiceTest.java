package ru.jobj4.urlshortcut.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.jobj4.urlshortcut.UrlShortcutApplication;
import ru.jobj4.urlshortcut.model.Site;
import ru.jobj4.urlshortcut.repository.SiteRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = UrlShortcutApplication.class)
class SimpleSiteServiceTest {

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteRepository siteRepository;

    @AfterEach
    public void cleanDB() {
        siteRepository.deleteAll();
    }

    @Test
    public void whenRegistration() {
        Site site = new Site();
        site.setSite("https://www.test.com");
        var siteDto = siteService.registration(site);
        assertThat(siteDto.isRegistration()).isTrue();
        assertThat(siteDto.getLogin()).isNotNull();
        assertThat(siteDto.getPassword()).isNotNull();
        assertThat(site.getId()).isNotNull();
    }

    @Test
    public void whenRegisteredSiteAlreadyExist() {
        Site site = new Site();
        site.setSite("https://www.test.com");
        Site site2 = new Site();
        site2.setSite("https://www.test.com");
        siteService.registration(site);
        assertThatThrownBy(() -> siteService.registration(site2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}