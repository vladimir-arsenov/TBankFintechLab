package com.example.tinkofffintech2024labdemo.service;

import com.example.tinkofffintech2024labdemo.dto.yandexapi.YandexIamTokenAcquiringResponse;
import com.example.tinkofffintech2024labdemo.dto.yandexapi.YandexTranslateResponse;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.model.Translation;
import com.example.tinkofffintech2024labdemo.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Реализация сервиса для управления переводом текста.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    private final RestTemplate restTemplate;

    private final TranslationRepository translationRepository;

    private String iamToken;

    @Value("${com.example.translate.folderId}")
    private String folderId;

    @Value("${com.example.translate.oAuthToken}")
    private String oAuthToken;

    /**
     * Переводит заданный текст с исходного языка на целевой язык,
     * путём разбиения текста на слова и перевода каждого слова в
     * отдельном потоке.
     *
     * @param text текст для перевода
     * @param sourceLang код исходного языка
     * @param targetLang код целевого языка
     * @param ipAddress IP-адрес клиента
     * @return переведенный текст
     */
    @SneakyThrows
    @Override
    public String translateText(String text, String sourceLang, String targetLang, String ipAddress) {
        if (iamToken == null) {
            acquireIamToken();
        }

        log.info("Translating text from {} to {} for IP: {}", sourceLang, targetLang, ipAddress);

        String[] words = text.trim().split("\\s+");
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(words.length, 10));
        List<Callable<String>> tasks = new ArrayList<>();

        for (String word : words) {
            tasks.add(() -> translateWord(word, sourceLang, targetLang));
            TimeUnit.MILLISECONDS.sleep(100); // необходимо из-за ограничений API: 20 запросов в 1 секунду
        }

        StringBuilder translatedText = new StringBuilder();
        List<Future<String>> futures = executor.invokeAll(tasks);
        for (Future<String> future : futures) {
            translatedText.append(future.get()).append(" ");
        }
        executor.shutdown();

        saveTranslation(ipAddress, text, translatedText.toString());

        log.info("Translation completed successfully");

        return translatedText.toString().trim();
    }


    /**
     * Переводит одно слово с исходного языка на целевой язык.
     *
     * @param word слово для перевода
     * @param sourceLang код исходного языка
     * @param targetLang код целевого языка
     * @return переведенное слово
     */
    private String translateWord(String word, String sourceLang, String targetLang) {
        log.info("Translating word: {}", word);

        String url = "https://translate.api.cloud.yandex.net/translate/v2/translate";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + iamToken);
        headers.set("Content-Type", "application/json");
        String requestJson = String.format("{\"folder_id\": \"%s\", \"texts\": [\"%s\"], \"targetLanguageCode\": \"%s\", \"sourceLanguageCode\": \"%s\"}", folderId, word, targetLang, sourceLang);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<YandexTranslateResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, YandexTranslateResponse.class);
            return Objects.requireNonNull(response.getBody()).getTranslations().get(0).getText();
        } catch (HttpClientErrorException e) {
            System.out.println(e);
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                if (e.getResponseBodyAsString().contains("unsupported source_language_code")) {
                    throw new UnsupportedLanguageException("Unsupported source language code");
                } else {
                    throw new TranslationResourceAccessException("Error accessing resource");
                }
            }
            throw e;
        }
    }

    /**
     * Сохраняет перевод с помощью репозитория.
     *
     * @param ipAddress IP-адрес клиента
     * @param text исходный текст
     * @param translatedText переведенный текст
     */
    private void saveTranslation(String ipAddress, String text, String translatedText) {
        translationRepository.save(new Translation(ipAddress, text, translatedText));
    }

    /**
     * Получает новый IAM токен.
     */
    private void acquireIamToken() {
        String url = "https://iam.api.cloud.yandex.net/iam/v1/tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = String.format("{\"yandexPassportOauthToken\": \"%s\"}", oAuthToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<YandexIamTokenAcquiringResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, YandexIamTokenAcquiringResponse.class);

        iamToken = Objects.requireNonNull(response.getBody()).getIamToken();

        log.info("IAM-token acquired");
    }

}

// TODO
//  unspoorted targetLangurage


// TODO
//  1) add update token if 401
//  4) proper encoding
//  7) remove all souts and clean up code
//  8) fix thing about exceptions