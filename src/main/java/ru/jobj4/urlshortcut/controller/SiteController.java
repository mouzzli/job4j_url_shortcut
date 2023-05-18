package ru.jobj4.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jobj4.urlshortcut.dto.SiteDto;
import ru.jobj4.urlshortcut.model.Site;
import ru.jobj4.urlshortcut.service.SiteService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class SiteController {

    private SiteService siteService;

    @PostMapping("/sign-up")
    public ResponseEntity<SiteDto> registration(@Valid @RequestBody Site site) {
        return new ResponseEntity<>(siteService.registration(site), HttpStatus.CREATED);
    }
}
