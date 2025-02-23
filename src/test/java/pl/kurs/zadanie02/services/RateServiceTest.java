package pl.kurs.zadanie02.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RateServiceTest {

    private ObjectMapper objectMapper;
    private IUrlBuilder urlBuilder;
    private RateService rateService;

    @Before
    public void init() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        urlBuilder = Mockito.mock(IUrlBuilder.class);
        rateService = new RateService(objectMapper, urlBuilder);
    }

    @Test
    public void testGetRate_successfulResponse() throws Exception {
        URL mockUrl = new URL("http://google.pl");
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);

        Mockito.when(urlBuilder.buildUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenReturn(mockUrl);

        String jsonResponse = "{\"result\": 1.25}";
        Mockito.when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(jsonResponse.getBytes()));

        JsonNode mockResultNode = Mockito.mock(JsonNode.class);
        Mockito.when(mockResultNode.asDouble()).thenReturn(1.25);
        JsonNode mockMainNode = Mockito.mock(JsonNode.class);
        Mockito.when(mockMainNode.get("result")).thenReturn(mockResultNode);
        Mockito.when(objectMapper.readTree(Mockito.anyString())).thenReturn(mockMainNode);

        double rate = rateService.getRate("USD", "EUR", 100);
        assertEquals(1.25, rate, 0.0);
    }

    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenInputCurrencyIncorrect() throws Exception {
        URL mockUrl = new URL("http://google.pl");
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);

        Mockito.when(urlBuilder.buildUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenReturn(mockUrl);

        Mockito.when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        JsonNode mockMainNode = Mockito.mock(JsonNode.class);
        Mockito.when(objectMapper.readTree(Mockito.anyString())).thenReturn(mockMainNode);

        rateService.getRate("INVALID", "EUR", 100);
    }


    @Test(expected = InvalidInputDataException.class)
    public void getRateThrowExceptionWhenOutputCurrencyIncorrect() throws Exception {
        URL mockUrl = new URL("http://google.pl");
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);

        Mockito.when(urlBuilder.buildUrl(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble())).thenReturn(mockUrl);

        Mockito.when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        JsonNode mockMainNode = Mockito.mock(JsonNode.class);
        Mockito.when(objectMapper.readTree(Mockito.anyString())).thenReturn(mockMainNode);

        rateService.getRate("USD", "INVALID", 100);
    }

}