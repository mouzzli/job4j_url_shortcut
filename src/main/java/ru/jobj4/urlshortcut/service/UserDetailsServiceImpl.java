package ru.jobj4.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.jobj4.urlshortcut.repository.SiteRepository;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private SiteRepository siteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalPerson = siteRepository.findByLogin(username);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(optionalPerson.get().getLogin(), optionalPerson.get().getPassword(), emptyList());
    }
}

