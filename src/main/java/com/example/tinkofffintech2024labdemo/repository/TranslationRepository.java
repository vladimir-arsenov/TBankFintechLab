package com.example.tinkofffintech2024labdemo.repository;

import com.example.tinkofffintech2024labdemo.model.Translation;

import java.util.List;

/**
 * Интерфейс для репозитория БД переводов.
 */
public interface TranslationRepository {
    /**
     * Сохраняет запись перевода в базе данных.
     *
     * @param translation запись перевода для сохранения
     */
    void save(Translation translation);

    /**
     * Возвращает все записи переводов из базы данных.
     *
     * @return список записей переводов
     */
    List<Translation> getAll();
}