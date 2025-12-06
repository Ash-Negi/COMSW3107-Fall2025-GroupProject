package processor;

import processor.ParkingProcessor;
import processor.HousingProcessor;
import processor.PopulationProcessor;
import processor.cache.StatsCache;

import java.util.Map;
import java.util.TreeMap;

/**
 * processing layer interface for the UI.
 *
 * This class delegates to PopulationProcessor, HousingProcessor,
 * and ParkingProcessor internally, but the UI should never access
 * those classes directly.
 */
public class CityStatsProcessor {

    private final PopulationProcessor population;
    private final HousingProcessor housing;
    private final ParkingProcessor parking;
    private final StatsCache cache;

    public CityStatsProcessor(PopulationProcessor population,
                              HousingProcessor housing,
                              ParkingProcessor parking,
                              StatsCache cache){
        this.population = population;
        this.parking = parking;
        this.housing = housing;
        this.cache = cache;
    }

    //Menu Option 1
    public int getTotalPopulation(){
        return population.getTotalPopulation();
    }

    //Menu Option 2
    public Map<String, Double> getFinesPerCapita() {
//        Map<String, Double> totalFines = parking.getTotalFinesByZip();
//        Map<String, Double> result = new TreeMap<>();
//
//        for (String zip : totalFines.keySet()) {
//            int pop = population.getPopulationForZip(zip);
//            if (pop > 0) {
//                double perCapita = totalFines.get(zip) / pop;
//                result.put(zip, perCapita);
//            }
//        }
//        return result;
        return cache.getFinesPerCapitaByZip();
    }

    // Menu option 3 — Average market value for a ZIP
    public int getAverageMarketValue(String zip) {
//        double avgValue = housing.getAverageMarketValue(zip);
//        return (int) Math.round(avgValue);
        if(!isValidZip(zip)) return 0;

        double output = cache.getAvgMarketValueByZip().getOrDefault(zip,0.0);
        return (int)Math.round(output);
    }

    // Menu option 4 — Average total livable area for a ZIP
    public int getAverageTotalLivableArea(String zip) {
//        double avgArea = housing.getAverageTotalLivableArea(zip);
//        return (int) Math.round(avgArea);
        if(!isValidZip(zip)) return 0;

        double output = cache.getAvgLivableAreaByZip().getOrDefault(zip,0.0);
        return (int)Math.round(output);
    }

    // Menu option 5 — Market value per capita for a ZIP
    public int getMarketValuePerCapita(String zip) {
//        double totalValue = housing.getTotalMarketValue(zip);
//        int pop = population.getPopulationForZip(zip);
//        if (pop == 0) return 0;
//        return (int) Math.round(totalValue / pop);
        if(!isValidZip(zip)) return 0;
        double totalMarketValue = cache.getTotalMarketValueByZip().getOrDefault(zip,0.0);
        int pop = population.getPopulationForZip(zip);
        if (pop == 0) return 0;
        return (int)Math.round(totalMarketValue/pop);
    }

    // Menu option 6 — Violations per capita for a ZIP
    public double getViolationsPerCapita(String zip) {
        // reject invalid calls immediately
//        if (zip == null || zip.length() < 5 || !zip.matches("\\d+")) {
//            return 0.0;   // or throw IllegalArgumentException
//        }
//        int count = parking.getViolationCountInZip(zip);
//        int pop = population.getPopulationForZip(zip);
//        if (pop == 0) return 0.0;
//        return (double) count / pop;
        if (!isValidZip(zip)) return 0.0;

        return cache.getViolationsPerCapitaByZip().getOrDefault(zip, 0.0);
    }

    // Menu option 7 — Average fine city-wide
    public double getAverageFineInPhiladelphia() {
        //This is not cached, not costly and only need to loop over violations once
        return parking.getAverageFineInPhiladelphia();
    }

    private boolean isValidZip(String zip) {
        return zip != null && zip.matches("\\d{5}");
    }

}
