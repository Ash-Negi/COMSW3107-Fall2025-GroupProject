import data.DataManager;
import data.models.PopulationData;
import processor.CityStatsProcessor;
import processor.HousingProcessor;
import processor.ParkingProcessor;
import processor.PopulationProcessor;

public class Main {
    public static void main(String[] args) throws Exception {
        DataManager dm = new DataManager();
        dm.loadAllData("csv", "src/main/resources/parking.csv",
                "src/main/resources/properties.csv",
                "src/main/resources/population.txt");

        ParkingProcessor parking = new ParkingProcessor(dm.getParkingViolations());
        HousingProcessor housing = new HousingProcessor(dm.getProperties());
        PopulationProcessor pop = new PopulationProcessor(
                dm.getPopulationMap().entrySet().stream()
                        .map(e -> new PopulationData(e.getKey(), e.getValue()))
                        .toList()
        );

        CityStatsProcessor stats = new CityStatsProcessor(pop, housing, parking);

        System.out.println(stats.getViolationsPerCapita("19104")); // valid
        System.out.println(stats.getViolationsPerCapita("abcde")); // INVALID – must return 0 or throw
        System.out.println(stats.getViolationsPerCapita(null));    // INVALID – must return 0 or throw
    }
}
