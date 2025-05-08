package pl.kurs.zadanie02.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CurrencyServiceTest {
    @Mock
    private IRateService rateService;

    private CurrencyService currencyService;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyService(rateService, 60);
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
        long shortCacheDuration = 1L;
        currencyService = new CurrencyService(rateService, shortCacheDuration);

        String from = "USD";
        String to = "GBP";

        Mockito.when(rateService.getRate(from, to)).thenReturn(0.75, 0.80);

        double result1 = currencyService.exchange(from, 100.0, to);
        assertEquals(75.0, result1, 0.01);

        Thread.sleep(11000);

        double result2 = currencyService.exchange(from, 100.0, to);
        assertEquals(80.0, result2, 0.01);

        Mockito.verify(rateService, Mockito.times(2)).getRate(from, to);
    }

    @Test
    public void testPopulateCacheThrowsException() throws InvalidInputDataException {
        Mockito.when(rateService.getRate(Mockito.anyString(), Mockito.anyString())).thenThrow(new IllegalStateException("Invalid currency"));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            currencyService.exchange("USD", 100.0, "EUR");
        });
        assertEquals("Invalid currency", thrown.getMessage());
    }

    @Test
    public void testValidExchange() throws InvalidInputDataException {
        double mockRate = 0.9;
        Mockito.when(rateService.getRate("USD", "EUR")).thenReturn(mockRate);

        double result = currencyService.exchange("USD", 100.0, "EUR");
        assertEquals(90.0, result, 0.0001);

        double result2 = currencyService.exchange("USD", 50.0, "EUR");
        assertEquals(45.0, result2, 0.0001);

        Mockito.verify(rateService, Mockito.times(1)).getRate("USD", "EUR");
    }

    @Test
    public void shouldExchangeRateThrowException() throws InvalidInputDataException {
        Mockito.when(rateService.getRate("USD", "EUR")).thenThrow(new IllegalStateException("Test exception"));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            currencyService.exchange("USD", 100.0, "EUR");
        });
        assertEquals("Test exception", thrown.getMessage());
    }

}