package ru.pscb.uprid.service;

import org.springframework.stereotype.Component;

@Component
public class NumberCheck {
    public boolean numberPassportCheck(String number, String series) {
        return number.matches("[-+]?\\d*\\.?\\d+") && series.matches("[-+]?\\d*\\.?\\d+");
    }
}
