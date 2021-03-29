package com.github.lukaslt1993.currency.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@RestController
public class ConverterController {

    @GetMapping
    public Double get(String fromCurrency, Double amount, String toCurrency) {
        MonetaryAmount amnt = Monetary.getDefaultAmountFactory().setCurrency(fromCurrency)
                .setNumber(amount).create();

        CurrencyConversion conversion = MonetaryConversions.getConversion(toCurrency);

        MonetaryAmount convertedAmount = amnt.with(conversion);

        return convertedAmount.getNumber().doubleValueExact();
    }

}
