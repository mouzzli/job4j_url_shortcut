package ru.jobj4.urlshortcut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UrlShortcutApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortcutApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UrlShortcutApplication.class);
	}
}
