package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.controller.TranslationController;
import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class TranslationControllerTest {

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private TranslationController translationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTranslateSuccess() {
        TranslationRequest request = new TranslationRequest();
        request.setText("Hello");
        request.setSourceLang("en");
        request.setTargetLang("ru");

        when(translationService.translateText(request.getText(), request.getSourceLang(), request.getTargetLang(), "127.0.0.1"))
                .thenReturn("Привет");

        ResponseEntity<String> response = translationController.translate(request, new MockHttpServletRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Привет", response.getBody());
    }

    @Test
    void testTranslateUnsupportedLanguageException() {
        TranslationRequest request = new TranslationRequest();
        request.setText("Hello");
        request.setSourceLang("unsupported");
        request.setTargetLang("ru");

        doThrow(new UnsupportedLanguageException("Unsupported source language code"))
                .when(translationService).translateText(request.getText(), request.getSourceLang(), request.getTargetLang(), "127.0.0.1");

        ResponseEntity<String> response = translationController.translate(request, new MockHttpServletRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Не найден язык исходного сообщения", response.getBody());
    }

    @Test
    void testTranslateTranslationResourceAccessException() {
        TranslationRequest request = new TranslationRequest();
        request.setText("Hello");
        request.setSourceLang("en");
        request.setTargetLang("ru");

        doThrow(new TranslationResourceAccessException("Error accessing resource"))
                .when(translationService).translateText(request.getText(), request.getSourceLang(), request.getTargetLang(), "127.0.0.1");

        ResponseEntity<String> response = translationController.translate(request, new MockHttpServletRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка доступа к ресурсу перевода", response.getBody());
    }
}
