package ru.jobj4.url_shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jobj4.url_shortcut.dto.SiteDto;
import ru.jobj4.url_shortcut.model.Site;
import ru.jobj4.url_shortcut.repository.SiteRepository;

import java.security.SecureRandom;
import java.util.Base64;

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

    private String randomString() {
        int stringLength = 12;
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[stringLength];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
