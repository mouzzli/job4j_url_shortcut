package ru.jobj4.url_shortcut.service;

import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.dto.UrlDto;
import ru.jobj4.url_shortcut.model.Url;

import java.util.List;
import java.util.Optional;

public interface UrlService {

    CodeDto convert(Url url);

    Optional<Url> findByCode(String code);

    List<UrlDto> findAll();
}
