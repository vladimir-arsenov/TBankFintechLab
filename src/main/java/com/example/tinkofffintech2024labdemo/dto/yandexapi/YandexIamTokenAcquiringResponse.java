package com.example.tinkofffintech2024labdemo.dto.yandexapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO для ответа о получении IAM токена от Yandex.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexIamTokenAcquiringResponse {

    @JsonProperty("iamToken")
    private String iamToken;
}
