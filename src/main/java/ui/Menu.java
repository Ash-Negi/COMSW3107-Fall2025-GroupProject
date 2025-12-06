package ui;

import processor.CityStatsProcessor;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private CityStatsProcessor processor;
    private Scanner scanner;

    public Menu(CityStatsProcessor processor) {
        this.processor = processor;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        while (true) {
            printMenu();

            String input = scanner.next();
            if (!input.matches("\\d+")) {
                continue;
            }

            int choice = Integer.parseInt(input);

            if (choice == 0) {
                return;
            }
            handleMenuOption(choice);
        }
    }

    private void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Total Population");
        System.out.println("2. Fines per Capita");
        System.out.println("3. Average Market Value");
        System.out.println("4. Average Livable Area");
        System.out.println("5. Market Value per Capita");
        System.out.println("6. Violations per Capita");
        System.out.println("7. Average Fine Amount");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private void handleMenuOption(int choice) {
        String zip;
        if (choice == 1) {
            System.out.println("Total Population: " + processor.getTotalPopulation());

        } else if (choice == 2) {
            Map<String, Double> fines = processor.getFinesPerCapita();
            for (Map.Entry<String, Double> entry : fines.entrySet()) {
                System.out.printf("%s %.4f%n", entry.getKey(), entry.getValue());
            }

        } else if (choice == 3) {
            zip = promptForZip();
            System.out.println(processor.getAverageMarketValue(zip));

        } else if (choice == 4) {
            zip = promptForZip();
            System.out.println(processor.getAverageTotalLivableArea(zip));

        } else if (choice == 5) {
            zip = promptForZip();
            System.out.println(processor.getMarketValuePerCapita(zip));

        } else if (choice == 6) {
            zip = promptForZip();
            System.out.printf("%.4f%n", processor.getViolationsPerCapita(zip));

        } else if (choice == 7) {
//            System.out.printf("%.2f%n", processor.getAverageFineInPhiladelphia());
            int avg = (int) Math.round(processor.getAverageFineInPhiladelphia());
            System.out.println(avg);
        } else {}
    }

    private String promptForZip() {
//        System.out.print("Enter a ZIP Code: ");
//        return scanner.next();
        while (true) {
            System.out.print("Enter a 5-digit ZIP Code: ");
            String zip = scanner.next();

            if (zip.matches("\\d{5}")) {
                return zip;
            }

            System.out.println("Invalid ZIP. Try again.");
        }
    }
}