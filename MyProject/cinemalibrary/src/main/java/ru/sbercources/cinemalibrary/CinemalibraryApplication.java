package ru.sbercources.cinemalibrary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class CinemalibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(CinemalibraryApplication.class, args);
        log.info("Swagger-ui run on: http://localhost:9090/swagger-ui/index.html");
    }
}
