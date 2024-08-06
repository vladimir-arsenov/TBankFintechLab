package com.example.tinkofffintech2024labdemo;

import com.example.tinkofffintech2024labdemo.repository.TranslationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class TinkoffFintech2024LabDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffFintech2024LabDemoApplication.class, args);
    }
}
