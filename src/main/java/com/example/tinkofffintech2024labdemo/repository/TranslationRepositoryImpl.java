package com.example.tinkofffintech2024labdemo.repository;

import com.example.tinkofffintech2024labdemo.model.Translation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория БД переводов.
 */
@Repository
@Slf4j
public class TranslationRepositoryImpl implements TranslationRepository {

    @Value("${com.example.database.url}")
    private String url;

    @Value("${com.example.database.user}")
    private String user;

    @Value("${com.example.database.password}")
    private String password;

    /**
     * Инициализирует базу данных, создавая таблицу переводов, если она не существует.
     */
    @PostConstruct
    @SneakyThrows
    public void init() {
        Class.forName("org.h2.Driver");
        String sql = """
                    CREATE TABLE IF NOT EXISTS translations (
                        ip_address VARCHAR(255) NOT NULL,
                        original_text TEXT NOT NULL,
                        translated_text TEXT NOT NULL
                    )
                    """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        log.info("DB initialized");
    }


    @Override
    @SneakyThrows
    public void save(Translation translation) {
        String sql = "INSERT INTO translations (ip_address, original_text, translated_text) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, translation.getIpAddress());
            statement.setString(2, translation.getOriginalText());
            statement.setString(3, translation.getTranslatedText());

            statement.executeUpdate();
        }

        log.info("Translation saved {}", translation);
    }


    @Override
    @SneakyThrows
    public List<Translation> getAll() {
        List<Translation> translations = new ArrayList<>();
        String sql = "SELECT ip_address, original_text, translated_text FROM translations";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Translation translation = new Translation();
                translation.setIpAddress(rs.getString("ip_address"));
                translation.setOriginalText(rs.getString("original_text"));
                translation.setTranslatedText(rs.getString("translated_text"));
                translations.add(translation);
            }
        }

        return translations;
    }

    @SneakyThrows
    public void clear() {
        String sql = "DROP TABLE IF EXISTS translations";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}

