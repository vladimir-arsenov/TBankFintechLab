package com.example.tinkofffintech2024labdemo.exception;

import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения для неподдерживаемых языков.
     *
     * @param ex исключение UnsupportedLanguageException
     * @return ответ с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(UnsupportedLanguageException.class)
    public ResponseEntity<String> handleUnsupportedLanguageException(UnsupportedLanguageException ex) {
        log.error("Unsupported language error: {}", ex.getMessage());
        return new ResponseEntity<>("Не найден язык исходного сообщения", HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключения, связанные с доступом к ресурсу перевода.
     *
     * @param ex исключение TranslationResourceAccessException
     * @return ответ с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(TranslationResourceAccessException.class)
    public ResponseEntity<String> handleTranslationResourceAccessException(TranslationResourceAccessException ex) {
        log.error("Translation resource access error: {}", ex.getMessage());
        return new ResponseEntity<>("Ошибка доступа к ресурсу перевода:\nПроблема при работе с Yandex API.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает ошибки валидации (валидация установлена только на {@link TranslationRequest} request в /translate).
     *
     * @param ex исключение валидации
     * @return ответ с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> errors.append(error.getDefaultMessage()).append(". "));
        return new ResponseEntity<>("Ошибка доступа к ресурсу перевода:\n" + errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает ошибки нечитаемых HTTP сообщений.
     *
     * @param ex исключение HttpMessageNotReadableException
     * @return ответ с сообщением об ошибке и статусом BAD_REQUEST
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HTTP message not readable error: {}", ex.getMessage());
        return new ResponseEntity<>("""
                Ошибка доступа к ресурсу перевода:
                Необходимо передать тело запроса
                {
                  "text": "",
                  "sourceLang": "" ,
                  "targetLang": ""
                }
                """, HttpStatus.BAD_REQUEST);
    }
}
