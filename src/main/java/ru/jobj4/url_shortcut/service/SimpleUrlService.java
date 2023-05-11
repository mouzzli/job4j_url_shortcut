package ru.jobj4.url_shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.repository.UrlRepository;

import static ru.jobj4.url_shortcut.util.RandomString.randomString;

@Service
@AllArgsConstructor
public class SimpleUrlService implements UrlService {

    private UrlRepository urlRepository;

    @Override
    public CodeDto convert(Url url) {
        url.setCode(randomString());
        return new CodeDto(urlRepository.save(url));
    }
}
