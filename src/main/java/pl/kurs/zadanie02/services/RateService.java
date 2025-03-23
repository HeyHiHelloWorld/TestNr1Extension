package pl.kurs.zadanie02.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.zadanie02.config.AppConfig;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

public class RateService implements IRateService {
    private final ObjectMapper objectMapper;
    private final IUrlBuilder urlBuilder;

    public RateService(ObjectMapper objectMapper, IUrlBuilder urlBuilder) {
        this.objectMapper = objectMapper;
        this.urlBuilder = urlBuilder;
    }


    @Override
    public double getRate(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException {
        if (amount <= 0) {
            throw new InvalidInputDataException("Amount must be positive");
        }

        try {
            StringBuilder response = createHttpRequest(currencyFrom, currencyTo, amount);
            JsonNode mainNode = objectMapper.readTree(response.toString());
            JsonNode resultNode = mainNode.get("result");

            return Optional.ofNullable(resultNode)
                    .map(JsonNode::asDouble)
                    .orElseThrow(() -> new InvalidInputDataException("Invalid currency output: " + currencyTo));
        } catch (IOException e) {
            throw new InvalidInputDataException("Invalid currency input: " + currencyFrom);
        }
    }

    private StringBuilder createHttpRequest(String currencyFrom, String currencyTo, double amount) throws InvalidInputDataException, IOException {
        URL urlRequest = urlBuilder.buildUrl(currencyFrom, currencyTo, amount);
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty(AppConfig.API_KEY_HEADER, AppConfig.PRIVATE_API_KEY);
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new InvalidInputDataException("Error in API request: Response code " + responseCode);
        }

        try (
                Scanner scanner = new Scanner(connection.getInputStream())
        ) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            return response;
        }
        finally {
            connection.disconnect();
        }
    }

}