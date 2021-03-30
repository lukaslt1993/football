package com.github.lukaslt1993.currency.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;

@RestController
public class ConverterController {

    @GetMapping
    public String get(String fromCurrency, String amount, String toCurrency) {
        MonetaryAmount amnt = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency)
                .setNumber(new BigDecimal(amount)).create();

        CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);

        MonetaryAmount convertedAmount = amnt.with(conversion);

        return convertedAmount.getNumber().toString();
    }

}
