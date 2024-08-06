package com.example.tinkofffintech2024labdemo.service;

/**
 * Интерфейс для сервиса перевода текста.
 */
public interface TranslationService {

    /**
     * Переводит заданный текст с исходного языка на целевой язык.
     *
     * @param text текст для перевода
     * @param sourceLang код исходного языка
     * @param targetLang код целевого языка
     * @param ipAddress IP-адрес клиента
     * @return переведенный текст
     */
    String translateText(String text, String sourceLang, String targetLang, String ipAddress);
}
