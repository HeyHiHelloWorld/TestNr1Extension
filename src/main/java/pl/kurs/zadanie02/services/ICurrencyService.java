package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

public interface ICurrencyService {
    double exchange(String currencyFrom, double value, String currencyTo) throws InvalidInputDataException;
}
