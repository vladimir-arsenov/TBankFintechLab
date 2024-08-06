package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationControllerTest {

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private TranslationController translationController;

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
}
