package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

public interface IRateService {
    double getRate(String currencyFrom, String currencyTo) throws InvalidInputDataException;
}
