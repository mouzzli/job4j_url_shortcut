package ru.jobj4.url_shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jobj4.url_shortcut.dto.CodeDto;
import ru.jobj4.url_shortcut.dto.UrlDto;
import ru.jobj4.url_shortcut.model.Url;
import ru.jobj4.url_shortcut.repository.SiteRepository;
import ru.jobj4.url_shortcut.repository.UrlRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.jobj4.url_shortcut.util.RandomString.randomString;

@Service
@AllArgsConstructor
public class SimpleUrlService implements UrlService {

    private UrlRepository urlRepository;
    private SiteRepository siteRepository;

    @Transactional
    @Override
    public CodeDto convert(Url url) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var site = siteRepository.findByLogin(authentication.getName());
        if (site.isEmpty()) {
            throw new NoSuchElementException("no such user with this login");
        }
        String urlHost = new URL(url.getUrl()).getHost();
        String siteHost = new URL(site.get().getSite()).getHost();
        if (urlHost.equals(siteHost)) {
            do {
                url.setCode(randomString());
            } while (urlRepository.findByCode(url.getCode()).isPresent());
            return new CodeDto(urlRepository.save(url));
        } else {
            throw new IllegalArgumentException(String.format("url does not belong to the registered site: %s", site.get().getSite()));
        }
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
