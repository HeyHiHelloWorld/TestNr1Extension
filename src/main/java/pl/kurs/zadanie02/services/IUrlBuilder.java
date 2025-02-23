package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.net.URL;

public interface IUrlBuilder {
    URL buildUrl(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException;
}
