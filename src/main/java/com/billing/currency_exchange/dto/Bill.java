package com.billing.currency_exchange.dto;

import com.billing.currency_exchange.config.UserType;
import lombok.Data;

import java.util.List;

@Data
public class Bill {

    private UserType user_type;
    private Double user_tenure;
    private List<Item> items;
    private Double totalAmt;
    private String original_currency;
    private String target_currency;


}
