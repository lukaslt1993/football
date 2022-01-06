package com.github.lukaslt1993.football.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
public class ConverterService {

    @Value("${application.converter.address}")
    private String converterAddress;

    private RestTemplate client = new RestTemplate();

    public BigDecimal convert (String fromCurrency, String amount, String toCurrency) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(converterAddress);
        builder.queryParam("fromCurrency", fromCurrency);
        builder.queryParam("amount", amount);
        builder.queryParam("toCurrency", toCurrency);
        String response = client.getForObject(builder.build().toUri(), String.class);
        return new BigDecimal(response);
    }

}
