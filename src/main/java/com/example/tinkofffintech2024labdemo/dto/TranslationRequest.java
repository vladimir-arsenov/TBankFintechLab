package com.example.tinkofffintech2024labdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для запроса перевода.
 */
@Data
public class TranslationRequest {

    @NotBlank(message = "Текст (text) не должен быть пустым")
    private String text;

    @NotBlank(message = "Исходный язык (sourceLang) не должен быть пустым")
    private String sourceLang;

    @NotBlank(message = "Целевой язык (targetLang) не должен быть пустым")
    private String targetLang;

}