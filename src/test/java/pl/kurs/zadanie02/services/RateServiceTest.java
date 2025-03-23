package pl.kurs.zadanie02.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie01.config.ObjectMapperHolder;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class RateServiceTest {

    private ObjectMapper objectMapper;

    @InjectMocks
    private RateService rateService;

    @Mock
    private IUrlBuilder urlBuilder;

    @Mock
    private HttpURLConnection urlMockConnection;


    @Before
    public void init() throws IOException, InvalidInputDataException {
        MockitoAnnotations.openMocks(this);
        objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        rateService = new RateService(objectMapper, urlBuilder);
    }

    @Test
    public void testGetRateSuccessfulResult() throws Exception {
        String currencyFrom = "EUR";
        String currencyTo = "USD";
        double amount = 100.0;

        String jsonResponse = "{\"result\": 1.1}";
        URL mockUrl = new URL("http://google.pl");

        Mockito.when(urlBuilder.buildUrl(currencyFrom, currencyTo, amount)).thenReturn(mockUrl);
        Mockito.when(urlMockConnection.getResponseCode()).thenReturn(200);
        Mockito.when(urlMockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));

        double expectedResult = rateService.getRate(currencyFrom, currencyTo, amount);

        assertEquals(1.1, expectedResult, 0.1);
    }

    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenInputCurrencyIsIncorrect() throws Exception {
        String currencyFrom = "INVALID";
        String invalidCurrencyTo = "USD";
        double amount = 100.0;

        URL mockUrl = new URL("http://google.pl");
        Mockito.when(urlBuilder.buildUrl(currencyFrom, invalidCurrencyTo, amount)).thenReturn(mockUrl);

        rateService.getRate(currencyFrom, invalidCurrencyTo, amount);
    }

    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenOutputCurrencyIsIncorrect() throws Exception {
        String currencyFrom = "USD";
        String invalidCurrencyTo = "INVALID";
        double amount = 100.0;

        URL mockUrl = new URL("http://google.pl");
        Mockito.when(urlBuilder.buildUrl(currencyFrom, invalidCurrencyTo, amount)).thenReturn(mockUrl);

        rateService.getRate(currencyFrom, invalidCurrencyTo, amount);
    }
}