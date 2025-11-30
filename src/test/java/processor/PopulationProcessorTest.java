package processor;

import data.models.PopulationData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopulationProcessorTest {
    @Test
    public void testTotalPopulation() {
        List<PopulationData> data = List.of(
                new PopulationData("19104", 1000),
                new PopulationData("19103", 2000),
                new PopulationData("00000", -5)  // should be ignored
        );

        PopulationProcessor pop = new PopulationProcessor(data);

        assertEquals(3000, pop.getTotalPopulation());
        assertEquals(1000, pop.getPopulationForZip("19104"));
        assertEquals(0, pop.getPopulationForZip("99999")); // invalid zip
    }
}
