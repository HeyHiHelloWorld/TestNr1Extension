package pl.kurs.zadanie02.services;

import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyCache {
    private Map<String, Double> currencyValueMap;
    private long creationTime;
    private final long refreshTime;
    private IRateService rateService;


    public CurrencyCache(long refreshTime, IRateService rateService) {
        this.currencyValueMap = new ConcurrentHashMap<>();
        this.creationTime = System.currentTimeMillis();
        this.refreshTime = refreshTime;
        this.rateService = rateService;
    }

    public Map<String, Double> getData(String currencyFrom, String currencyTo) throws InvalidInputDataException {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - creationTime) >= refreshTime) {
            currencyValueMap.clear();
            populateCache(currencyFrom, currencyTo);
            creationTime = currentTime;
        }
        return currencyValueMap;
    }

    public Double getValue(String key) {
        return currencyValueMap.get(key);
    }

    private void populateCache(String currencyFrom, String currencyTo) throws InvalidInputDataException {
        double rate = rateService.getRate(currencyFrom, currencyTo);
        currencyValueMap.put(currencyFrom + "-" + currencyTo, rate);
    }


}
