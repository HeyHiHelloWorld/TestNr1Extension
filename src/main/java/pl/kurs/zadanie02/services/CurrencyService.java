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
            throw new InvalidInputDataException("Amount cannot must be positive");
        }
        String cacheKey = currencyFrom + "-" + currencyTo;

            Map<String, Double> cacheData = currencyCache.getData(currencyFrom, currencyTo, amount);
            Double rate = cacheData.get(cacheKey);

                if (rate == null) {
                    synchronized (this) {
                        rate = rateService.getRate(currencyFrom, currencyTo, amount);
                        cacheData.put(cacheKey, rate);
                    }
                }
            return rate * amount;
        }

}
