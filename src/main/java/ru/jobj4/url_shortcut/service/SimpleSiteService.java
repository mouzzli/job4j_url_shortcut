package ru.jobj4.url_shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jobj4.url_shortcut.dto.SiteDto;
import ru.jobj4.url_shortcut.model.Site;
import ru.jobj4.url_shortcut.repository.SiteRepository;

import static ru.jobj4.url_shortcut.util.RandomString.randomString;

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
