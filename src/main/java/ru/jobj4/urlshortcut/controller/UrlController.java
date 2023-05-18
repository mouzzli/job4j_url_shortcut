package ru.jobj4.urlshortcut.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.urlshortcut.dto.CodeDto;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.model.Url;
import ru.jobj4.urlshortcut.service.UrlService;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@AllArgsConstructor
public class UrlController {

    private UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<CodeDto> convert(@Valid @RequestBody Url url) {
        try {
            return new ResponseEntity<>(urlService.convert(url), HttpStatus.OK);
        } catch (DataIntegrityViolationException | MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("url %s already exist", url.getUrl()));
        }
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> convert(@PathVariable String code) {
        Url url = urlService.findByCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("code not found : %s", code)));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(url.getUrl())).build();
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<UrlDto>> statistic() {
        var body = urlService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<?> handle(NoSuchElementException e) {
        String body = e.getMessage();
        log.error(body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<?> handle(IllegalArgumentException e) {
        String body = e.getMessage();
        log.error(body);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", e.getMessage()));
    }
}
