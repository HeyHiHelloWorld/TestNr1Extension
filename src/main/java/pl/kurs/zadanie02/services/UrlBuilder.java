package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.config.AppConfig;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder implements IUrlBuilder {
    @Override
    public URL buildUrl(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException {
        try {
            return new URL(AppConfig.API_MAIN_PAGE + AppConfig.CONVERT_TO + currencyTo +
                    AppConfig.CONVERT_FROM + currencyFrom + AppConfig.AMOUNT + amount);
        } catch (MalformedURLException e) {
            throw new InvalidInputDataException("Incorrect details input");
        }
    }

}