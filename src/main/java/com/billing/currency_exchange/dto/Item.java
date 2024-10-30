package com.billing.currency_exchange.dto;

import com.billing.currency_exchange.config.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private String name;
    private ItemCategory category;
}
