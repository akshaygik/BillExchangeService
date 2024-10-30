package com.billing.currency_exchange.controller;

import com.billing.currency_exchange.dto.Bill;
import com.billing.currency_exchange.dto.ExchangeRate;
import com.billing.currency_exchange.service.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchangeRates/{baseCurrency}")
    public ExchangeRate getExchangeRates(@PathVariable String baseCurrency) {
        logger.info("Fetching exchange rate data for currency {}", baseCurrency);
        ExchangeRate exchangeRate = exchangeService.getExchangeRates(baseCurrency);
        if (exchangeRate != null) {
            logger.info("Successfully fetched exchange rates for: {}", baseCurrency);
        } else {
            logger.warn("Received null response from exchange rates API for: {}", baseCurrency);
        }
        return exchangeRate;
    }

    @GetMapping("/calculate")
    public Double generateTotalBill(@RequestBody Bill bill) {
        logger.info("Calculating bill with amount {} and source currency {}", bill.getTotalAmt(), bill.getOriginal_currency());
        Double generatedTotalBill =  exchangeService.calculateBill(bill);
        if (generatedTotalBill != null) {
            logger.info("Successfully calculated bill amount: {}", generatedTotalBill);
        } else {
            logger.warn("Received null amount");
        }
        return generatedTotalBill;
    }
}
