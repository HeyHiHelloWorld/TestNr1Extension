package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Optional;

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

        Optional<Double> cachedRate = cache.get(cacheKey);

        double rate;

        if (cachedRate.isPresent()) {
            rate = cachedRate.get();
        } else {
            rate = rateService.getRate(currencyFrom, currencyTo);
            cache.compute(cacheKey, rate);
        }

        return rate * amount;
    }
}