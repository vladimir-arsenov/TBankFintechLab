package com.example.tinkofffintech2024labdemo.repository;

import com.example.tinkofffintech2024labdemo.model.Translation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TranslationRepositoryImplTest {

    private TranslationRepositoryImpl translationRepository;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        translationRepository = new TranslationRepositoryImpl();

        Field url = TranslationRepositoryImpl.class.getDeclaredField("url");
        url.setAccessible(true);
        url.set(translationRepository, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        Field user = TranslationRepositoryImpl.class.getDeclaredField("user");
        user.setAccessible(true);
        user.set(translationRepository, "sa");

        Field password = TranslationRepositoryImpl.class.getDeclaredField("password");
        password.setAccessible(true);
        password.set(translationRepository, "");

        // Initialize in-memory database
        translationRepository.init();
    }

    @AfterAll
    static void afterAll() {

    }

    @AfterEach
    @SneakyThrows
    void tearDown() {
        translationRepository.clear();
    }

    @Test
    void shouldSaveTranslation() {
        Translation translation = new Translation("127.0.0.1", "Hello", "Привет");
        translationRepository.save(translation);

        List<Translation> translations = translationRepository.getAll();

        System.out.println(translations);

        assertEquals(1, translations.size());
        assertEquals("127.0.0.1", translations.get(0).getIpAddress());
        assertEquals("Hello", translations.get(0).getOriginalText());
        assertEquals("Привет", translations.get(0).getTranslatedText());
    }

    @Test
    void shouldGetAllRequests() {
        Translation translation1 = new Translation("127.0.0.1", "Hello", "Привет");
        Translation translation2 = new Translation("127.0.0.2", "World", "Мир");
        translationRepository.save(translation1);
        translationRepository.save(translation2);

        List<Translation> translations = translationRepository.getAll();

        assertEquals(2, translations.size());
        assertEquals(translation1, translations.get(0));
        assertEquals(translation2, translations.get(1));
    }
}
