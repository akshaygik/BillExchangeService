package com.billing.currency_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExchangeRate {

    @JsonProperty("conversion_rates")
    private Map<String, Double> rates;
}
