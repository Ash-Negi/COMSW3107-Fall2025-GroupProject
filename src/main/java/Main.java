import data.DataManager;
import data.models.PopulationData;
import processor.*;
import java.io.File;
import java.util.List;
import processor.CityStatsProcessor;
import processor.HousingProcessor;
import processor.ParkingProcessor;
import processor.PopulationProcessor;



public class Main {

    public static void main(String[] args) {

        if (args.length != 4) {
            System.err.println("Incorrect number of arguments.");
            System.err.println("Usage:");
            System.err.println("  java Main <csv|json> <parkingFile> <propertyFile> <populationFile>");
            return;
        }

        String format = args[0];
        String parkingFile = args[1];
        String propertyFile = args[2];
        String populationFile = args[3];

        if (!format.equals("csv") && !format.equals("json")) {
            System.err.println("Error: First argument must be 'csv' or 'json'.");
            return;
        }

        if (fileMissing(parkingFile) ||
                fileMissing(propertyFile) ||
                fileMissing(populationFile))
        {
            System.err.println("Error: One or more input files do not exist:");
            if (fileMissing(parkingFile)) System.err.println("  Missing: " + parkingFile);
            if (fileMissing(propertyFile)) System.err.println("  Missing: " + propertyFile);
            if (fileMissing(populationFile)) System.err.println("  Missing: " + populationFile);
            return;
        }

        try {
            // Load data
            DataManager dm = new DataManager();
            dm.loadAllData(format, parkingFile, propertyFile, populationFile);

            ParkingProcessor parking = new ParkingProcessor(dm.getParkingViolations());
            HousingProcessor housing = new HousingProcessor(dm.getProperties());

            List<PopulationData> popList = dm.getPopulationMap()
                    .entrySet().stream()
                    .map(e -> new PopulationData(e.getKey(), e.getValue()))
                    .toList();

            PopulationProcessor pop = new PopulationProcessor(popList);

            CityStatsProcessor cityStats = new CityStatsProcessor(pop, housing, parking);

            // Start UI
            ui.Menu menu = new ui.Menu(cityStats);
            menu.start();

        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static boolean fileMissing(String path) {
        File f = new File(path);
        return !f.exists() || f.isDirectory();
    }
}
