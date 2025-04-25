package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CurrencyService implements ICurrencyService {
    private IRateService rateService;
    private final long cacheDurationMillis;
    private volatile CurrencyCache currencyCache;

    public CurrencyService(IRateService rateService, long cacheDurationSeconds) {
        this.rateService = rateService;
        this.cacheDurationMillis = TimeUnit.SECONDS.toMillis(cacheDurationSeconds);
        this.currencyCache = new CurrencyCache(cacheDurationMillis, rateService);
    }


    @Override
    public double exchange(String currencyFrom, double amount, String currencyTo) throws InvalidInputDataException {
        if (amount <= 0) {
            throw new InvalidInputDataException("Amount must be positive");
        }
        String cacheKey = currencyFrom + "-" + currencyTo;

        Map<String, Double> cacheData = currencyCache.getData(currencyFrom, currencyTo);
        double rate;

        try {
            rate = cacheData.computeIfAbsent(cacheKey, key -> {
                try {
                    return rateService.getRate(currencyFrom, currencyTo);
                } catch (InvalidInputDataException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InvalidInputDataException) {
                throw (InvalidInputDataException) e.getCause();
            }

            throw e;
        }

        return rate * amount;
    }
}