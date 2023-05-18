package ru.jobj4.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.urlshortcut.UrlShortcutApplication;
import ru.jobj4.urlshortcut.dto.SiteDto;
import ru.jobj4.urlshortcut.model.Site;
import ru.jobj4.urlshortcut.service.SiteService;

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
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT,
                String.format("site %s already exist", site.getSite())));

        this.mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(site)))
                .andExpect(status().isConflict());
    }
}