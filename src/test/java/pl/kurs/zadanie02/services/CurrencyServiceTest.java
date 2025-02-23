package pl.kurs.zadanie02.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CurrencyServiceTest {

    @Mock
    private IRateService rateService;

    @Mock
    private CurrencyCache currencyCache;


    @InjectMocks
    private CurrencyService currencyService;
    private static final long CACHE_DURATION = 10;

    @Before
    public void init() throws InvalidInputDataException {
        currencyService = new CurrencyService(rateService, CACHE_DURATION);
        currencyCache = new CurrencyCache(CACHE_DURATION);
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = InvalidInputDataException.class)
    public void shouldThrowExceptionWhenAmountIsNegative() throws InvalidInputDataException {
        currencyService.exchange("EUR", -1, "PLN");
    }

    @Test
    public void testPopulateCache() throws InvalidInputDataException {
        Mockito.when(rateService.getRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenReturn(2.0);

        currencyCache.getData("USD", "EUR", 10);

        Double value = currencyCache.getValue("USD-EUR");

        assertNotNull(value);
        assertEquals(0.0, value, 0.0);
    }

    @Test
    public void testCacheRefresh() throws InvalidInputDataException, InterruptedException {
        Mockito.when(rateService.getRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenReturn(2.0).thenReturn(3.0);

        currencyCache.getData("USD", "EUR", 10);
        Double value1 = currencyCache.getValue("USD-EUR");
        assertNotNull(value1);
        assertEquals(0.0, value1, 0.0);

        Thread.sleep(CACHE_DURATION + 1000); //

        currencyCache.getData("USD", "EUR", 10);
        Double value2 = currencyCache.getValue("USD-EUR");
        assertNotNull(value2);
        assertEquals(0.0, value2, 0.0);
    }

    @Test
    public void testPopulateCache_throwsInvalidInputDataException() throws InvalidInputDataException {
        Mockito.when(rateService.getRate(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenThrow(new InvalidInputDataException("Invalid currency"));

        currencyCache.getData("USD", "EUR", 10);
    }


}