package com.billing.currency_exchange.service;

import com.billing.currency_exchange.config.ItemCategory;
import com.billing.currency_exchange.config.UserType;
import com.billing.currency_exchange.dto.Bill;
import com.billing.currency_exchange.dto.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.billing.currency_exchange.config.AppConstants.exchangeRateUrl;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private RestTemplate restTemplate;
    private static ExchangeRate exchangeRate;

    /**
    This method fetches exchange rates for mentioned currency
    Input: source currency code
    Output: Exchange rates in all standard international currencies
     **/
    @Override
    public ExchangeRate getExchangeRates(String sourceCurrency) {
        String url = exchangeRateUrl+ sourceCurrency;
        exchangeRate = restTemplate.getForObject(url, ExchangeRate.class);
        return exchangeRate;
    }

    /**
     This method calculates discount as per user type and tenure as well as item type in bill.
     Applies discount to total bill amount, deducts flat discount rate if amount is greater than 100$.
     After that it fetches exchange rates in source currency and converts total bill in target currency using exchange rates.
     Input: Bill details including item list, user type, tenure and source, target currency
     Output: Calculated and converted total bill amount
     **/
    @Override
    public Double calculateBill(Bill bill) {
        calculateDiscount(bill);
        exchangeRate = getExchangeRates(bill.getOriginal_currency());
        Double targetCurrencyRate = exchangeRate.getRates().get(bill.getTarget_currency());
        Double exchangedTotalAmt = bill.getTotalAmt() * targetCurrencyRate;
        // rounding off till 2 digits
        String formattedAmt = String.format("%.2f", exchangedTotalAmt);
        return Double.parseDouble(formattedAmt);
    }

    private void calculateDiscount(Bill bill) {

        boolean IsGroceryPresent = bill.getItems().stream().anyMatch(item -> item.getCategory().equals(ItemCategory.GROCERY));
        double originalBillAmt = bill.getTotalAmt();
        if(!IsGroceryPresent) {
            int percentageDiscount = checkForPercentageDiscount(bill);
            if(percentageDiscount > 0) {
                double discountedAmt = ((double) percentageDiscount /100) * originalBillAmt;
                originalBillAmt = originalBillAmt - discountedAmt;
            }
        }

       bill.setTotalAmt(checkForFlatDiscount(originalBillAmt));
    }

    private int checkForPercentageDiscount(Bill bill) {
        if(bill.getUser_type().equals(UserType.EMPLOYEE)) {
            return 30;
        } else if(bill.getUser_type().equals(UserType.AFFILIATE)) {
            return 10;
        } else if(bill.getUser_type().equals(UserType.OTHER) && bill.getUser_tenure() > 2.0) {
            return 5;
        } else {
            return 0;
        }
    }

    private double checkForFlatDiscount(double originalBillAmt) {
        // Calculate the number of $100 increments
        int discountCount = (int) (originalBillAmt / 100);
        // Calculate total discount
        double totalDiscount = discountCount * 5.0;
        return originalBillAmt - totalDiscount;
    }
}
