package com.example.tinkofffintech2024labdemo.exception;

import com.example.tinkofffintech2024labdemo.dto.TranslationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO
    //  doc and log everywhere again


    @ExceptionHandler(UnsupportedLanguageException.class)
    public ResponseEntity<String> handle1(UnsupportedLanguageException ex) {
        System.out.println("UnsupportedLanguageException");
        return new ResponseEntity<>("Не найден язык исходного сообщения", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TranslationResourceAccessException.class)
    public ResponseEntity<String> handle2(TranslationResourceAccessException ex) {
        System.out.println("TranslationResourceAccessException");
        return new ResponseEntity<>("Ошибка доступа к ресурсу перевода:\nПроблема при работе с Yandex API.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает ошибки валидации (JSON {@link TranslationRequest} полученного /translate).
     *
     * @param ex исключение валидации
     * @return сообщение об ошибке
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.append(error.getDefaultMessage()).append(". ");
        });
        return new ResponseEntity<>("Ошибка доступа к ресурсу перевода:\n" + errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handle4(HttpMessageNotReadableException ex) {
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

