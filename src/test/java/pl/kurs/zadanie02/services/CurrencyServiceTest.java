package pl.kurs.zadanie02.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import static org.junit.Assert.assertEquals;

public class CurrencyServiceTest {
    @Mock
    private IRateService rateService;

    private CurrencyCache currencyCache;
    private CurrencyService currencyService;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyService(rateService, 60);
        currencyCache = new CurrencyCache(60, rateService);
    }

    @Test(expected = InvalidInputDataException.class)
    public void shouldThrowExceptionWhenAmountIsNegative() throws InvalidInputDataException {
        currencyService.exchange("EUR", -1, "PLN");
    }

    @Test
    public void shouldExchangeCorrectlyUsingCachedRate() throws Exception {
        String currencyFrom = "USD";
        String currencyTo = "EUR";
        double amount = 100.0;
        double rate = 0.85;

        Mockito.when(rateService.getRate(currencyFrom, currencyTo)).thenReturn(rate);

        double result1 = currencyService.exchange(currencyFrom, amount, currencyTo);
        assertEquals(85.0, result1, 0.01);


        double result2 = currencyService.exchange(currencyFrom, amount, currencyTo);
        assertEquals(85.0, result2, 0.01);

        Mockito.verify(rateService, Mockito.times(1)).getRate(currencyFrom, currencyTo);
    }

    @Test
    public void shouldUpdateCacheAfterExpiration() throws Exception {
        long shortCacheDuration = 1;
        currencyService = new CurrencyService(rateService, shortCacheDuration);

        String from = "USD";
        String to = "GBP";

        Mockito.when(rateService.getRate(from, to)).thenReturn(0.75, 0.80);

        double result1 = currencyService.exchange(from, 100.0, to);
        assertEquals(75.0, result1, 0.01);

        Thread.sleep(1200);

        double result2 = currencyService.exchange(from, 100.0, to);
        assertEquals(80.0, result2, 0.01);

        Mockito.verify(rateService, Mockito.times(2)).getRate(from, to);
    }

    @Test
    public void testPopulateCacheThrowsInvalidInputDataException() throws InvalidInputDataException {
        Mockito.when(rateService.getRate(Mockito.anyString(), Mockito.anyString())).thenThrow(new InvalidInputDataException("Invalid currency"));

        currencyCache.getData("USD", "EUR");
    }


}