package ru.jobj4.url_shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.dto.UrlDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.repository.UrlRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public Optional<Url> findByCode(String code) {
        var url = urlRepository.findByCode(code);
        url.ifPresent(url1 -> urlRepository.incrementCount(url1.getId()));
        return url;
    }

    @Override
    public List<UrlDto> findAll() {
        var urlList = urlRepository.findAll();
        return urlList.stream()
                .map(UrlDto::new)
                .collect(Collectors.toList());
    }
}
