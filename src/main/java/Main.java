import data.DataManager;
import data.models.PopulationData;
import processor.*;
import processor.cache.StatsCache;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        //  Validate runtime arguments
        if (args.length != 4) {
            System.err.println("Incorrect number of arguments.");
            System.err.println("Usage: java Main <csv|json> <parkingFile> <propertyFile> <populationFile>");
            return;
        }

        //need error handling for inappropiate number

        String format = args[0];
        String parkingFile = args[1];
        String propertyFile = args[2];
        String populationFile = args[3];

        if (!format.equals("csv") && !format.equals("json")) {
            System.err.println("Error: first argument must be 'csv' or 'json'.");
            return;
        }

        if (missingFile(parkingFile) || missingFile(propertyFile) || missingFile(populationFile)) {
            System.err.println("Error: One or more input files do not exist:");
            if (missingFile(parkingFile)) System.err.println("  Missing: " + parkingFile);
            if (missingFile(propertyFile)) System.err.println("  Missing: " + propertyFile);
            if (missingFile(populationFile)) System.err.println("  Missing: " + populationFile);
            return;
        }

        try {
            //  Load raw data using DataManager
            DataManager dm = new DataManager();
            dm.loadAllData(format, parkingFile, propertyFile, populationFile);

            ParkingProcessor parking = new ParkingProcessor(dm.getParkingViolations());
            HousingProcessor housing = new HousingProcessor(dm.getProperties());

            List<PopulationData> popList = dm.getPopulationMap()
                    .entrySet().stream()
                    .map(e -> new PopulationData(e.getKey(), e.getValue()))
                    .toList();
            PopulationProcessor population = new PopulationProcessor(popList);

            //  Build the StatsCache (Singleton)
            StatsCache.initializeCacheBuild(housing, parking, population);

            CityStatsProcessor stats = new CityStatsProcessor(
                    population,
                    housing,
                    parking,
                    StatsCache.getInstance()
            );

            //  Launch Menu UI
            ui.Menu menu = new ui.Menu(stats);
            menu.start();

        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static boolean missingFile(String path) {
        File f = new File(path);
        return !f.exists() || f.isDirectory();
    }
}
