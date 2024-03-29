package ru.jobj4.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.urlshortcut.dto.CodeDto;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.mapper.CodeDtoMapper;
import ru.jobj4.urlshortcut.mapper.UrlDtoMapper;
import ru.jobj4.urlshortcut.model.Url;
import ru.jobj4.urlshortcut.repository.SiteRepository;
import ru.jobj4.urlshortcut.repository.UrlRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.jobj4.urlshortcut.util.RandomString.randomString;

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
        if (!urlHost.equals(siteHost)) {
            throw new IllegalArgumentException(String.format("url does not belong to the registered site: %s", site.get().getSite()));
        }
        do {
            url.setCode(randomString());
        } while (urlRepository.findByCode(url.getCode()).isPresent());
        try {
            return CodeDtoMapper.INSTANCE.toCodeDto(urlRepository.save(url));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("url %s already exist", url.getUrl()));
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
                .map(UrlDtoMapper.INSTANCE::convertToUrlDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Url> findByUrl(String url) {
        return urlRepository.findByUrl(url);
    }
}
