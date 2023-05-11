package ru.jobj4.url_shortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.service.UrlService;

import javax.validation.Valid;
import java.net.URI;

@RestController
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

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> convert(@PathVariable String code) {
        Url url = urlService.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("code not found : %s", code)));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(url.getUrl())).build();
    }
}
