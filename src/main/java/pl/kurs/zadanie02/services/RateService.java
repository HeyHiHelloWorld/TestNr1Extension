package pl.kurs.zadanie02.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.zadanie02.config.AppConfig;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RateService implements IRateService {
    private final ObjectMapper objectMapper;
    private final IUrlBuilder urlBuilder;

    public RateService(ObjectMapper objectMapper, IUrlBuilder urlBuilder) {
        this.objectMapper = objectMapper;
        this.urlBuilder = urlBuilder;
    }

    @Override
    public double getRate(String currencyFrom, String currencyTo) throws InvalidInputDataException {
        URL urlRequest = urlBuilder.buildUrl(currencyFrom, currencyTo, 1.0);

        try {
            HttpURLConnection connection = openConnection(urlRequest);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(AppConfig.API_KEY_HEADER, AppConfig.PRIVATE_API_KEY_VALUE);
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new InvalidInputDataException("Cannot retrieve data from Api, error code: " + responseCode);
            }
            try (
                    InputStream inputStream = connection.getInputStream();
            ) {
                JsonNode jsonNode = objectMapper.readTree(inputStream);
                return jsonNode.asDouble();
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            throw new InvalidInputDataException("Cannot retrieve rate from server.");
        }
    }

    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

}