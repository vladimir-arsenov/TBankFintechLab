package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.exception.GlobalExceptionHandler;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.service.TranslationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
@ExtendWith(MockitoExtension.class)
public class TranslationControllerAdviceTest {

    private MockMvc mockMvc;

    @MockBean
    private TranslationServiceImpl translationService;

    @BeforeEach
    void setUp() {
        TranslationController translationController = new TranslationController(translationService);
        mockMvc = MockMvcBuilders.standaloneSetup(translationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testUnsupportedLanguageException() throws Exception {
        when(translationService.translateText(anyString(), anyString(), anyString(), anyString())).thenThrow(new UnsupportedLanguageException("Unsupported source language code"));

        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"text\":\"Hello\",\"sourceLang\":\"invalid\",\"targetLang\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Не найден язык исходного сообщения"));
    }

    @Test
    void testTranslationResourceAccessException() throws Exception {
        when(translationService.translateText(anyString(), anyString(), anyString(), anyString())).thenThrow(new TranslationResourceAccessException("Error accessing resource"));

        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"text\":\"Hello\",\"sourceLang\":\"en\",\"targetLang\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка доступа к ресурсу перевода:\nПроблема при работе с Yandex API."));
    }

    @Test
    void testValidationErrors() throws Exception {
        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{\"text\":\"\",\"sourceLang\":\"en\",\"targetLang\":\"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка доступа к ресурсу перевода:\nТекст (text) не должен быть пустым. "));
    }

    @Test
    void testHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("""
                        Ошибка доступа к ресурсу перевода:
                        Необходимо передать тело запроса
                        {
                          "text": "",
                          "sourceLang": "" ,
                          "targetLang": ""
                        }
                        """));
    }
}
