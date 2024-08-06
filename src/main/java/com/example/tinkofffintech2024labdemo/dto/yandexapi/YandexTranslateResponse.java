package com.example.tinkofffintech2024labdemo.dto.yandexapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * DTO для ответа о переводе от Yandex.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexTranslateResponse {

    @JsonProperty("translations")
    private List<YTranslation> translations;


    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YTranslation {
        @JsonProperty("text")
        private String text;
    }
}
