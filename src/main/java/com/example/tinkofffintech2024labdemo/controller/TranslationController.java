package com.example.tinkofffintech2024labdemo.controller;

import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import com.example.tinkofffintech2024labdemo.exception.TranslationResourceAccessException;
import com.example.tinkofffintech2024labdemo.exception.UnsupportedLanguageException;
import com.example.tinkofffintech2024labdemo.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
