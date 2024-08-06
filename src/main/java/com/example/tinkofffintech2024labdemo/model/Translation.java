package com.example.tinkofffintech2024labdemo.model;

import lombok.*;

/**
 * Модель для представления перевода в БД.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Translation {

    private String ipAddress;
    private String originalText;
    private String translatedText;
}
