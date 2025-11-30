package processor;

import data.models.ParkingViolation;
import data.models.PopulationData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityStatsProcessorTest {
    @Test
    public void testViolationsPerCapita() {
        PopulationProcessor pop = new PopulationProcessor(
                List.of(new PopulationData("19104", 2000))
        );

        HousingProcessor housing = new HousingProcessor(List.of());
        ParkingProcessor parking = new ParkingProcessor(
                List.of(
                        new ParkingViolation(LocalDateTime.now(), 50, "desc", "V1", "PA", "ID1", "19104"),
                        new ParkingViolation(LocalDateTime.now(), 50, "desc", "V2", "PA", "ID2", "19104")
                )
        );

        CityStatsProcessor stats = new CityStatsProcessor(pop, housing, parking);

        assertEquals(0.001, stats.getViolationsPerCapita("19104"), 0.00001);
        assertEquals(0.0, stats.getViolationsPerCapita("BADZIP"), 0.001);
    }
}
