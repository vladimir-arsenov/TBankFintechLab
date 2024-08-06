package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.controller.TranslationController;
import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.exception.GlobalExceptionHandler;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class TranslationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private TranslationController translationController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(translationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
}
