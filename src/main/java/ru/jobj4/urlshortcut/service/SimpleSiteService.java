package ru.jobj4.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.jobj4.urlshortcut.dto.SiteDto;
import ru.jobj4.urlshortcut.model.Site;
import ru.jobj4.urlshortcut.repository.SiteRepository;

import static java.util.Collections.emptyList;
import static ru.jobj4.urlshortcut.util.RandomString.randomString;

@Service
@AllArgsConstructor
public class SimpleSiteService implements SiteService, UserDetailsService {
    private SiteRepository siteRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SiteDto registration(Site site) {
        do {
            site.setLogin(randomString());
        } while (siteRepository.findByLogin(site.getLogin()).isPresent());
        var password = randomString();
        site.setPassword(bCryptPasswordEncoder.encode(password));
        try {
            siteRepository.save(site);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("site %s already exist", site.getSite())
            );
        }
        return new SiteDto(true, site.getLogin(), password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalPerson = siteRepository.findByLogin(username);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(optionalPerson.get().getLogin(), optionalPerson.get().getPassword(), emptyList());
    }
}
