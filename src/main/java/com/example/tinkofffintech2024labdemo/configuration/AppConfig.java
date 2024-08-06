package com.example.tinkofffintech2024labdemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс для создания и настройки бинов приложения.
 */
@Configuration
public class AppConfig {

    /**
     * Создает и возвращает бин {@link RestTemplate}.
     *
     * @return экземпляр {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
