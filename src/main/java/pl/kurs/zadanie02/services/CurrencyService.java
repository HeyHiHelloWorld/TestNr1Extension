package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CurrencyService implements ICurrencyService {
    private IRateService rateService;
    private final long cacheDurationMillis;


    public CurrencyService(IRateService rateService, long cacheDurationSeconds) {
        this.rateService = rateService;
        this.cacheDurationMillis = TimeUnit.SECONDS.toMillis(cacheDurationSeconds);
    }


    @Override
    public synchronized double exchange(String currencyFrom, double amount, String currencyTo) throws InvalidInputDataException {
        if (amount <= 0) {
            throw new InvalidInputDataException("Wartość waluty powinna być większa niż 0");
        }

        String cacheKey = currencyFrom + "-" + currencyTo;
        try {
            CurrencyCache currencyCache = new CurrencyCache(cacheDurationMillis);
            Map<String, Double> cacheData = currencyCache.getData(currencyFrom, currencyTo, amount);
            Double rate = cacheData.get(cacheKey);

            if (rate == null) {
                rate = rateService.getRate(currencyFrom, currencyTo, amount);
                cacheData.put(cacheKey, rate);
            }

            return rate * amount;
        } catch (Exception e) {
            throw new InvalidInputDataException("Błąd pobierania danych: " + e.getMessage());
        }
    }

}
