package processor;

import processor.ParkingProcessor;
import processor.HousingProcessor;
import processor.PopulationProcessor;

import java.util.Map;
import java.util.TreeMap;

public class CityStatsProcessor {

    PopulationProcessor population;
    HousingProcessor housing;
    ParkingProcessor parking;

    public CityStatsProcessor(PopulationProcessor population, HousingProcessor housing, ParkingProcessor parking){
        this.population = population;
        this.parking = parking;
        this.housing = housing;
    }

    //Menu Option 1
    public int getTotalPopulation(){
        return population.getTotalPopulation();
    }

    //Menu Option 2
    public Map<String, Double> getFinesPerCapita() {
        Map<String, Double> totalFines = parking.getTotalFinesByZip();
        Map<String, Double> result = new TreeMap<>();

        for (String zip : totalFines.keySet()) {
            int pop = population.getPopulationForZip(zip);
            if (pop > 0) {
                double perCapita = totalFines.get(zip) / pop;
                result.put(zip, perCapita);
            }
        }

        return result;
    }

    // Menu option 3 — Average market value for a ZIP
    public int getAverageMarketValue(String zip) {
        double avgValue = housing.getAverageMarketValue(zip);
        return (int) Math.round(avgValue);
    }

    // Menu option 4 — Average total livable area for a ZIP
    public int getAverageTotalLivableArea(String zip) {
        double avgArea = housing.getAverageTotalLivableArea(zip);
        return (int) Math.round(avgArea);
    }

    // Menu option 5 — Market value per capita for a ZIP
    public int getMarketValuePerCapita(String zip) {
        double totalValue = housing.getTotalMarketValue(zip);
        int pop = population.getPopulationForZip(zip);
        if (pop == 0) return 0;
        return (int) Math.round(totalValue / pop);
    }

    // Menu option 6 — Violations per capita for a ZIP
    public double getViolationsPerCapita(String zip) {
        // reject invalid calls immediately
        if (zip == null || zip.length() < 5 || !zip.matches("\\d+")) {
            return 0.0;   // or throw IllegalArgumentException
        }
        int count = parking.getViolationCountInZip(zip);
        int pop = population.getPopulationForZip(zip);
        if (pop == 0) return 0.0;
        return (double) count / pop;
    }

    // Menu option 7 — Average fine city-wide
    public double getAverageFineInPhiladelphia() {
        return parking.getAverageFineInPhiladelphia();
    }



}
