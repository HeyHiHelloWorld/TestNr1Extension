package pl.kurs.zadanie02.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.kurs.zadanie01.config.ObjectMapperHolder;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class RateServiceTest {

    private ObjectMapper objectMapper;
    private RateService rateService;

    @Mock
    private IUrlBuilder urlBuilder;

    @Mock
    private HttpURLConnection mockConnection;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        rateService = new RateService(objectMapper, urlBuilder);
    }


    @Test(expected = InvalidInputDataException.class)
    public void testGetRateHttpError() throws Exception {
        URL dummyUrl = new URL("http", "dummy", 80, "dummy", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
                Mockito.when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
                return connection;
            }
        });
        Mockito.when(urlBuilder.buildUrl("USD", "EUR", 1.0)).thenReturn(dummyUrl);

        rateService.getRate("USD", "EUR");
    }

    @Test
    public void testGetRateSuccessfulResult() throws Exception {
        String currencyFrom = "EUR";
        String currencyTo = "USD";
        double expectedRate = 1.1;
        String jsonResponse = "1.1";

        URL mockUrl = new URL("http", "dummy", 80, "dummy", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) {
                return mockConnection;
            }
        });
        Mockito.when(urlBuilder.buildUrl(currencyFrom, currencyTo, 1.0)).thenReturn(mockUrl);
        Mockito.when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));
        Mockito.when(mockConnection.getInputStream()).thenReturn(inputStream);
        double expectedResult = rateService.getRate(currencyFrom, currencyTo);

        assertEquals(expectedRate, expectedResult, 0.1);
    }

    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenInputCurrencyIsIncorrect() throws Exception {
        String invalidCurrencyFrom = "INVALID";
        String currencyTo = "USD";

        Mockito.when(urlBuilder.buildUrl(invalidCurrencyFrom, currencyTo, 1.0))
                .thenThrow(new InvalidInputDataException("Incorrect details input"));

        rateService.getRate(invalidCurrencyFrom, currencyTo);
    }



    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenOutputCurrencyIsIncorrect() throws Exception {
        String currencyFrom = "USD";
        String invalidCurrencyTo = "INVALID";
        URL mockUrl = new URL("http", "dummy", 80, "dummy", new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) {
                return mockConnection;
            }
        });

        Mockito.when(urlBuilder.buildUrl(currencyFrom, invalidCurrencyTo, 1.0))
                .thenReturn(mockUrl);
        Mockito.when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        rateService.getRate(currencyFrom, invalidCurrencyTo);
    }
}