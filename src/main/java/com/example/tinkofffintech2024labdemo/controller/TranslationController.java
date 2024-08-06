package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов на перевод текста.
 */
@RestController
@RequestMapping("/translate")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    /**
     * Обрабатывает запрос на перевод текста.
     *
     * @param request     запрос с данными для перевода (исходный язык, язык перевода, текст)
     * @param httpRequest запрос HTTP
     * @return переведенный текст
     */
    @PostMapping
    public ResponseEntity<String> translate(@Valid @RequestBody TranslationRequest request, HttpServletRequest httpRequest) {
        String translatedText = translationService.translateText(
                request.getText(), request.getSourceLang(),
                request.getTargetLang(), httpRequest.getRemoteAddr());
        return ResponseEntity.ok(translatedText);
    }
}
