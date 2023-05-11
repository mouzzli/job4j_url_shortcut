package ru.jobj4.url_shortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.service.UrlService;

import javax.validation.Valid;

@RestController
@RequestMapping("/url")
@AllArgsConstructor
public class UrlController {

    private UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<CodeDto> convert(@Valid @RequestBody Url url) {
        try {
            return new ResponseEntity<>(urlService.convert(url), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("url %s already exist", url.getUrl()));
        }
    }
}
