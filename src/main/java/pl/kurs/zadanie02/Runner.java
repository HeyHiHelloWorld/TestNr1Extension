package pl.kurs.zadanie02;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kurs.zadanie02.config.ObjectMapperHolder;
import pl.kurs.zadanie02.exceptions.InvalidInputDataException;
import pl.kurs.zadanie02.services.CurrencyService;
import pl.kurs.zadanie02.services.ICurrencyService;
import pl.kurs.zadanie02.services.IRateService;
import pl.kurs.zadanie02.services.IUrlBuilder;
import pl.kurs.zadanie02.services.RateService;
import pl.kurs.zadanie02.services.UrlBuilder;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        ObjectMapper objectMapper = ObjectMapperHolder.INSTANCE.getObjectMapper();
        IUrlBuilder urlBuilder = new UrlBuilder();
        IRateService rateService = new RateService(objectMapper, urlBuilder);
        ICurrencyService currencyService = new CurrencyService(rateService, 10);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w serwisie wymiany walut!");
        printOptions();
        int option = -1;

        do {
            try {
                switch ((option = scanner.nextInt())) {
                    case 1:
                        scanner.nextLine();
                        System.out.println("Jaką walutę chciałbyś wymienić?:");
                        String from = scanner.nextLine().toUpperCase();
                        System.out.println("W jakiej ilości?:");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.println("Na jaką inną walutę?:");
                        String to = scanner.nextLine().toUpperCase();
                        double exchange = currencyService.exchange(from, amount, to);
                        System.out.println("Otrzymasz " + exchange + " " + to.toUpperCase());
                        printOptions();
                        break;
                    case 0:
                        System.out.println("Koniec");
                        break;
                    default:
                        System.err.println("Nie rozpoznano opcji!");
                        printOptions();
                }
            } catch (InvalidInputDataException e) {
                handleException(e);
            } catch (InputMismatchException e) {
                handleException(e);
                scanner.nextLine();
            }
        } while (option != 0);
        scanner.close();
    }

    private static void printOptions() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("1 - wymiana gotówki");
        System.out.println("0 - wyjście");
    }

    private static void handleException(Throwable e) {
        if (e.getMessage() != null)
            System.err.println(e.getMessage());
        else
            System.err.println("Błąd wprowadzania!");
        printOptions();
    }
}

