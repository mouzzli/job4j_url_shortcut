package ru.jobj4.url_shortcut.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import ru.jobj4.url_shortcut.UrlShortcutApplication;
import ru.jobj4.url_shortcut.dto.UrlDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.repository.SiteRepository;
import ru.jobj4.url_shortcut.repository.UrlRepository;

import java.net.MalformedURLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest(classes = UrlShortcutApplication.class)
class SimpleUrlServiceTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private SiteRepository siteRepository;

    @AfterEach
    public void cleanDB() {
        urlRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test_user")
    @Sql(scripts = "/scripts/dml_insert_site.sql")
    public void whenConvert() throws MalformedURLException {
        Url url = new Url();
        url.setUrl("https://www.example.com/test");
        var codeDto = urlService.convert(url);
        assertThat(codeDto).isNotNull();
    }

    @Test
    @WithMockUser(username = "test_user")
    @Sql(scripts = "/scripts/dml_insert_site.sql")
    public void whenConvertWithNotBelongUrl() {
        Url url = new Url();
        url.setUrl("https://www.test.com/test");
        assertThatThrownBy(() -> urlService.convert(url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("url does not belong to the registered site: https://www.example.com");
    }

    @Test
    @WithMockUser(username = "user")
    public void whenConvertWithUSerNotFound() {
        Url url = new Url();
        url.setUrl("https://www.example.com/test");
        assertThatThrownBy(() -> urlService.convert(url))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such user with this login");
    }


    @Test
    @Sql(scripts = "/scripts/dml_insert_new_url.sql")
    public void whenFindByCodeSuccessfully() {
        Url url = new Url();
        url.setUrl("https://www.test.com");
        url.setCode("23wore4523");
        var optionalUrl = urlService.findByCode(url.getCode());
        assertThat(optionalUrl.get().getUrl()).isEqualTo(url.getUrl());
    }

    @Test
    @Sql(scripts = "/scripts/dml_insert_new_url.sql")
    public void whenFindByCodeNotFound() {
        Url url = new Url();
        url.setCode("23wore3");
        var optionalUrl = urlService.findByCode(url.getCode());
        assertThat(optionalUrl).isEmpty();
    }


    @Test
    @Sql(scripts = "/scripts/dml_insert_new_url.sql")
    public void whenFindAll() {
        var savedUrlDroList = urlService.findAll();
        var urlDtoList = List.of(
                new UrlDto("https://www.test.com", 0),
                new UrlDto("https://www.example.com", 0));
        assertThat(savedUrlDroList).isEqualTo(urlDtoList);
    }

    @Test
    @Sql(scripts = "/scripts/dml_insert_new_url.sql")
    public void whenFindByCodeThenCounterIncrement() {
        Url url = new Url();
        url.setUrl("https://www.test.com");
        url.setCode("23wore4523");
        urlService.findByCode(url.getCode());
        urlService.findByCode(url.getCode());
        urlService.findByCode(url.getCode());
        var optionalUrl = urlService.findByUrl(url.getUrl());
        assertThat(optionalUrl.get().getCounter()).isEqualTo(3);
    }
}