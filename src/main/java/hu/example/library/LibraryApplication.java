package hu.example.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Library API — Java 25, Spring Boot 3.x, executable JAR.
 * Migrated from WAR/Tomcat deployment (SpringBootServletInitializer removed).
 */
@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
