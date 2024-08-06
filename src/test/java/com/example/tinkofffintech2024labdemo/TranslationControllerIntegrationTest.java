package com.example.tinkofffintech2024labdemo;

import com.example.tinkofffintech2024labdemo.controller.TranslationController;
import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
class TranslationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @Test
    void testTranslateSuccess() throws Exception {
        TranslationRequest request = new TranslationRequest();
        request.setText("Hello");
        request.setSourceLang("en");
        request.setTargetLang("ru");

        when(translationService.translateText(request.getText(), request.getSourceLang(), request.getTargetLang(), "127.0.0.1"))
                .thenReturn("Привет");

        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Hello\", \"sourceLang\": \"en\", \"targetLang\": \"ru\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Привет"));
    }

    @Test
    void testTranslateValidationError() throws Exception {
        mockMvc.perform(post("/translate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sourceLang\": \"en\", \"targetLang\": \"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка доступа к ресурсу перевода"));
    }
}
