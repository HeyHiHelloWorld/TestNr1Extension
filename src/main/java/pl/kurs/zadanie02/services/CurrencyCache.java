package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyCache {
    private Map<String, Double> currencyValueMap;
    private long creationTime;
    private final long refreshTime;
    private IRateService rateService;

    public CurrencyCache(long refreshTime) {
        this.currencyValueMap = new ConcurrentHashMap<>();
        this.creationTime = System.currentTimeMillis();
        this.refreshTime = refreshTime;
    }

    public Map<String, Double> getData(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - creationTime) >= refreshTime) {
            currencyValueMap.clear();
            populateCache(currencyFrom, currencyTo, amount);
            creationTime = currentTime;
        }
        return currencyValueMap;

    }

    public Double getValue(String key) {
        return currencyValueMap.get(key);
    }

    private void populateCache(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException {
        double rate = rateService.getRate(currencyFrom, currencyTo, amount);
        currencyValueMap.put(currencyFrom + "-" + currencyTo, rate);
    }


}
