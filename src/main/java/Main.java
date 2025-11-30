import data.DataManager;
import data.models.PopulationData;
import processor.*;

public class Main {

    /**
     * DEVELOPMENT MODE ONLY
     * This main() is just a temporary harness to manually test data → processor flow.
     * Not part of final UI layer. Will be modified when UI is implemented.
     */
    public static void main(String[] args) throws Exception {
        DataManager dm = new DataManager();
        dm.loadAllData("csv",
                "src/main/resources/parking.csv",
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

        // TODO: hook this into your UI layer (menu)
        System.out.println(stats.getTotalPopulation());
    }
}
