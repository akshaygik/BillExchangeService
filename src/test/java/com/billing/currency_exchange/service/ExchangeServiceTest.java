package com.billing.currency_exchange.service;

import com.billing.currency_exchange.config.ItemCategory;
import com.billing.currency_exchange.config.UserType;
import com.billing.currency_exchange.dto.Bill;
import com.billing.currency_exchange.dto.ExchangeRate;
import com.billing.currency_exchange.dto.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeServiceImpl apiService;

    @Test
    public void testGetExchangeRatesAndCalculateBill() {
        ExchangeRate expectedRates = new ExchangeRate();
        expectedRates.setRates(Map.of("USD", 1.0, "AED", 3.6725, "AFN", 67.192, "INR", 84.0883));

        when(restTemplate.getForObject("https://v6.exchangerate-api.com/v6/9ab9899aaf7a7c6ba792ab36/latest/USD", ExchangeRate.class))
                .thenReturn(expectedRates);

        ExchangeRate actualRates = apiService.getExchangeRates("USD");

        assertNotNull(actualRates);
        assertEquals(expectedRates.getRates(), actualRates.getRates());
        verify(restTemplate, times(1)).getForObject("https://v6.exchangerate-api.com/v6/9ab9899aaf7a7c6ba792ab36/latest/USD", ExchangeRate.class);

        Bill bill = new Bill();
        bill.setTotalAmt(200.0);
        List<Item> items = new ArrayList<>();
        items.add(new Item("Bulb", ItemCategory.ELECTRONICS));
        items.add(new Item("Notebook", ItemCategory.STATIONARY));
        bill.setItems(items);
        bill.setOriginal_currency("USD");
        bill.setTarget_currency("INR");
        bill.setUser_type(UserType.OTHER);
        bill.setUser_tenure(2.5);

        Double calculatedBill = apiService.calculateBill(bill);

        assertNotNull(calculatedBill);
        assertEquals(15556.34, calculatedBill);
    }
}
