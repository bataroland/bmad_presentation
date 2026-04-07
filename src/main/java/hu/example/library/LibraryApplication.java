package hu.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Library API — Spring Boot 1.5 alkalmazás, WAR-ként deployolható Tomcat-re.
 */
@SpringBootApplication
public class LibraryApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LibraryApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
