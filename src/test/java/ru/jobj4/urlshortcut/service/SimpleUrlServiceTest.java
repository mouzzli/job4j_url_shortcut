package ru.jobj4.urlshortcut.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.urlshortcut.UrlShortcutApplication;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.model.Url;
import ru.jobj4.urlshortcut.repository.SiteRepository;
import ru.jobj4.urlshortcut.repository.UrlRepository;

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
    public void whenConvertWithUserNotFound() {
        Url url = new Url();
        url.setUrl("https://www.example.com/test");
        assertThatThrownBy(() -> urlService.convert(url))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("no such user with this login");
    }

    @Test
    @WithMockUser(username = "test_user")
    @Sql(scripts = "/scripts/dml_insert_site.sql")
    @Sql(scripts = "/scripts/dml_insert_new_url.sql")
    public void whenConvertWithUrlAlreadyExist() {
        Url url = new Url();
        url.setUrl("https://www.example.com");
        assertThatThrownBy(() -> urlService.convert(url))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(String.format("url %s already exist", url.getUrl()))
                .satisfies(e -> assertThat(((ResponseStatusException) e).getStatus()).isEqualTo(HttpStatus.CONFLICT));
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