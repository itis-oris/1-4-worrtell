package dev.wortel.meshok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan("entity")
@EnableJpaRepositories({"dev.wortel.meshok.repository", "dev.wortel.meshok.security"})
@EnableScheduling
@EnableRetry
@SpringBootApplication(scanBasePackages = "dev.wortel.meshok")
public class MigratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigratorApplication.class, args);
    }

}