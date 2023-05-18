package ru.jobj4.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.jobj4.urlshortcut.UrlShortcutApplication;
import ru.jobj4.urlshortcut.dto.CodeDto;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.model.Url;
import ru.jobj4.urlshortcut.service.UrlService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void whenConvertedSuccessfully() throws Exception {
        Url url = new Url();
        CodeDto codeDto = new CodeDto();
        String generatingCode = "generatedCode";
        codeDto.setCode(generatingCode);
        when(urlService.convert(url)).thenReturn(codeDto);
        url.setUrl("https:/www.test.com");

        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(codeDto.getCode()));
    }

    @Test
    @WithMockUser
    public void whenConvertedFailed() throws Exception {
        Url url = new Url();
        url.setUrl("https:/www.test.com");
        when(urlService.convert(url)).thenThrow(new DataIntegrityViolationException(""));

        this.mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(url)))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenRedirectSuccessfully() throws Exception {
        String code = "a4vf654dsebskl";
        Url url = new Url();
        url.setUrl("https://job4j.ru");
        var optionalUrl = Optional.of(url);
        when(urlService.findByCode(code)).thenReturn(optionalUrl);

        this.mockMvc.perform(get("/redirect/" + code))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", url.getUrl()));
    }

    @Test
    public void whenRedirectFailed() throws Exception {
        String code = "a4vf654dsebskl";
        when(urlService.findByCode(code)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/redirect/" + code))
             .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void whenStatistic() throws Exception {
        var listDto = List.of(
                new UrlDto("https://www.test.ru", 10),
                new UrlDto("https://www.test2.ru", 4));

        when(urlService.findAll()).thenReturn(listDto);

        this.mockMvc.perform(get("/statistic"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"site\":\"https://www.test.ru\",\"total\":10},{\"site\":\"https://www.test2.ru\",\"total\":4}]"));
    }
}