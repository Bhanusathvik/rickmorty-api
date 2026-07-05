package com.rickmorty.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RickMortyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RickMortyApiApplication.class, args);
    }
}
