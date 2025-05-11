package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyService implements ICurrencyService {
    private final IRateService rateService;
    private final long cacheDurationMillis;
    private Map<String, Double> currencyCache;

    public CurrencyService(IRateService rateService, long cacheDurationMillis) {
        this.rateService = rateService;
        this.cacheDurationMillis = cacheDurationMillis;
        this.currencyCache = new ConcurrentHashMap<>();
    }

    @Override
    public double exchange(String currencyFrom, double amount, String currencyTo) throws InvalidInputDataException {
        if (amount <= 0) {
            throw new InvalidInputDataException("Amount must be positive");
        }
        String cacheKey = currencyFrom + "-" + currencyTo;

        double rate = currencyCache.computeIfAbsent(cacheKey, k -> {
            try {
                return rateService.getRate(currencyFrom, currencyTo);
            } catch (InvalidInputDataException e) {
                throw new IllegalStateException("Failed to fetch rate", e);
            }
        });
        return rate * amount;
    }
}