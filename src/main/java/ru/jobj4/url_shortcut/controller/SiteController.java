package ru.jobj4.url_shortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.url_shortcut.dto.SiteDto;
import ru.jobj4.url_shortcut.model.Site;
import ru.jobj4.url_shortcut.service.SiteService;

import javax.validation.Valid;

@RestController
@RequestMapping("/site")
@AllArgsConstructor
public class SiteController {

    private SiteService siteService;

    @PostMapping("/sign-up")
    public ResponseEntity<SiteDto> registration(@Valid @RequestBody Site site) {
        try {
            return new ResponseEntity<>(siteService.registration(site), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("site %s already exist", site.getSite())
            );
        }
    }
}
