package com.example.tinkofffintech2024labdemo.service;

import com.example.tinkofffintech2024labdemo.dto.yandexapi.YandexIamTokenAcquiringResponse;
import com.example.tinkofffintech2024labdemo.dto.yandexapi.YandexTranslateResponse;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.model.Translation;
import com.example.tinkofffintech2024labdemo.repository.TranslationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslationServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TranslationRepository translationRepository;

    private TranslationServiceImpl translationService;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        translationService = new TranslationServiceImpl(restTemplate, translationRepository);

        Field iamTokenField = TranslationServiceImpl.class.getDeclaredField("iamToken");
        iamTokenField.setAccessible(true);
        iamTokenField.set(translationService, "testToken");
    }


    @Test
    void translateWordShouldTranslate() {
        YandexTranslateResponse yandexResponse = new YandexTranslateResponse();
        YandexTranslateResponse.YTranslation translation = new YandexTranslateResponse.YTranslation();
        translation.setText("Привет");
        yandexResponse.setTranslations(Collections.singletonList(translation));

        when(restTemplate.exchange(anyString(), any(), any(), eq(YandexTranslateResponse.class)))
                .thenReturn(new ResponseEntity<>(yandexResponse, HttpStatus.OK));

        String result = translationService.translateText("Hello", "en", "ru", "127.0.0.1");

        assertEquals("Привет", result);
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void translateWordShouldThrowUnsupportedLanguageException() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(YandexTranslateResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", "{\"message\":\"unsupported source_language_code\"}".getBytes(), null));

        ExecutionException exception = assertThrows(ExecutionException.class, () -> {
            translationService.translateText("", "", "", "");
        });

        assertEquals(exception.getCause().getClass(), UnsupportedLanguageException.class);
    }

    @Test
    void translateWordShouldThrowResourceAccessException() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(YandexTranslateResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", "".getBytes(), null));

        ExecutionException exception = assertThrows(ExecutionException.class, () -> {
            translationService.translateText("", "", "", "");
        });

        assertEquals(exception.getCause().getClass(), TranslationResourceAccessException.class);
    }

    @Test
    @SneakyThrows
    void translateWordShouldAcquireAndSetIamToken() {
        YandexIamTokenAcquiringResponse iamTokenAcquiringResponse = new YandexIamTokenAcquiringResponse();
        iamTokenAcquiringResponse.setIamToken("Token");
        Field iamTokenField = TranslationServiceImpl.class.getDeclaredField("iamToken");
        iamTokenField.setAccessible(true);
        iamTokenField.set(translationService, null);

        when(restTemplate.exchange(anyString(), any(), any(), eq(YandexIamTokenAcquiringResponse.class)))
                .thenReturn(new ResponseEntity<>(iamTokenAcquiringResponse, HttpStatus.OK));
        // null приведёт к ошибке, но дальнейшая работа метода не важна в этом тесте
        when(restTemplate.exchange(anyString(), any(), any(), eq(YandexTranslateResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertNull(iamTokenField.get(translationService));

        try {
            translationService.translateText("", "","","");
        } catch (Exception e) {}

        assertEquals("Token", iamTokenField.get(translationService));
    }
}
