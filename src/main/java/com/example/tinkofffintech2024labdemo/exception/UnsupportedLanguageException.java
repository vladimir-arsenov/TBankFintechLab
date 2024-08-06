package com.example.tinkofffintech2024labdemo.exception;

/**
 * Исключение, выбрасываемое при использовании неподдерживаемого или неправильно указанного клиентом языка.
 */
public class UnsupportedLanguageException extends RuntimeException {
    public UnsupportedLanguageException(String message) {
        super(message);
    }
}
