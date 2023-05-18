package ru.jobj4.urlshortcut.service;

import ru.jobj4.urlshortcut.dto.CodeDto;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.model.Url;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface UrlService {

    CodeDto convert(Url url) throws MalformedURLException;

    Optional<Url> findByCode(String code);

    List<UrlDto> findAll();

    Optional<Url> findByUrl(String url);
}
