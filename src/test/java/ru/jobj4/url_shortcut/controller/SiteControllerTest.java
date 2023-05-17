package ru.jobj4.url_shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.jobj4.url_shortcut.UrlShortcutApplication;
import ru.jobj4.url_shortcut.dto.SiteDto;
import ru.jobj4.url_shortcut.model.Site;
import ru.jobj4.url_shortcut.service.SiteService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteService siteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test    public void whenRegistrationSuccessfully() throws Exception {
        Site site = new Site();
        site.setSite("https://www.test.ru");
        SiteDto siteDto = new SiteDto(true, "login", "password");
        when(siteService.registration(site)).thenReturn(siteDto);
        this.mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("registration").value(true))
                .andExpect(jsonPath("login").value("login"))
                .andExpect(jsonPath("password").value("password"));

        verify(siteService, times(1)).registration(site);
    }

    @Test
    public void whenRegistrationFailed() throws Exception {
        Site site = new Site();
        site.setSite("https://www.test.ru");
        when(siteService.registration(site))
                .thenThrow(new DataIntegrityViolationException(String.format("site %s already exist", site.getSite())));

        this.mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isConflict());
    }
}