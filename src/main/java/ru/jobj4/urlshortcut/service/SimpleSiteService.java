package ru.jobj4.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jobj4.urlshortcut.dto.SiteDto;
import ru.jobj4.urlshortcut.model.Site;
import ru.jobj4.urlshortcut.repository.SiteRepository;

import static ru.jobj4.urlshortcut.util.RandomString.randomString;

@Service
@AllArgsConstructor
public class SimpleSiteService implements SiteService {
    private SiteRepository siteRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SiteDto registration(Site site) {
        site.setLogin(randomString());
        var password = randomString();
        site.setPassword(bCryptPasswordEncoder.encode(password));
        siteRepository.save(site);
        return new SiteDto(true, site.getLogin(), password);
    }

}
