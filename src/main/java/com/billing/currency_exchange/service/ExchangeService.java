package com.billing.currency_exchange.service;

import com.billing.currency_exchange.dto.Bill;
import com.billing.currency_exchange.dto.ExchangeRate;

public interface ExchangeService {

    ExchangeRate getExchangeRates(String sourceCurrency);
    Double calculateBill(Bill bill);
}
