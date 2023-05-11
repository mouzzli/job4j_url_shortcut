package ru.jobj4.url_shortcut.service;

import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.model.Url;

public interface UrlService {

    CodeDto convert(Url url);
}
