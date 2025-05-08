package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

public class CurrencyService implements ICurrencyService {
    private final IRateService rateService;
    private final long cacheDurationMillis;
    private CurrencyCacheService<String, Double> cache;

    public CurrencyService(IRateService rateService, long cacheDurationMillis) {
        this.rateService = rateService;
        this.cacheDurationMillis = cacheDurationMillis;
        this.cache = new CurrencyCacheService<>();
    }

    @Override
    public double exchange(String currencyFrom, double amount, String currencyTo) throws InvalidInputDataException {
        if (amount <= 0) {
            throw new InvalidInputDataException("Amount must be positive");
        }
        String cacheKey = currencyFrom + "-" + currencyTo;

        double rate = cache.computeIfAbsent(cacheKey, k -> {
            try {
                return rateService.getRate(currencyFrom, currencyTo);
            } catch (InvalidInputDataException e) {
                throw new IllegalStateException("Failed to fetch rate", e);
            }
        });
        return rate * amount;
    }
}