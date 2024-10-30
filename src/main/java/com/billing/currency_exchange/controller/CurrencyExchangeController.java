package com.billing.currency_exchange.controller;

import com.billing.currency_exchange.dto.Bill;
import com.billing.currency_exchange.dto.ExchangeRate;
import com.billing.currency_exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

    @Autowired
    private ExchangeService exchangeService;


    @GetMapping("/exchangeRates/{baseCurrency}")
    public ExchangeRate getExchangeRates(@PathVariable String baseCurrency) {
        return exchangeService.getExchangeRates(baseCurrency);
    }

    @GetMapping("/calculate")
    public Double generateTotalBill(@RequestBody Bill bill) {
        return exchangeService.calculateBill(bill);
    }
}
