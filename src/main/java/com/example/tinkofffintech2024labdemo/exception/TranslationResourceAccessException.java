package com.example.tinkofffintech2024labdemo.exception;

/**
 * Исключение, выбрасываемое при ошибке при работе с API Yandex-a.
 */
public class TranslationResourceAccessException extends RuntimeException {
    public TranslationResourceAccessException(String message) {
        super(message);
    }
}