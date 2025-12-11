package processor;

import data.models.PopulationData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopulationProcessorTest {
    @Test
    public void testTotalPopulation() {
        PopulationProcessor pop = new PopulationProcessor(
                List.of(
                        new PopulationData("19104", 2000),
                        new PopulationData("19103", 3000)
                )
        );

        assertEquals(5000, pop.getTotalPopulation());
    }

    @Test
    public void testPopulationLookup() {
        PopulationProcessor pop = new PopulationProcessor(
                List.of(
                        new PopulationData("19104", 2000)
                )
        );

        assertEquals(2000, pop.getPopulationForZip("19104"));
    }

    @Test
    public void testMissingZipReturnsZero() {
        PopulationProcessor pop = new PopulationProcessor(
                List.of(new PopulationData("19104", 2000))
        );

        assertEquals(0, pop.getPopulationForZip("99999"));
    }

    @Test
    public void testGetAllZipCodes() {
        PopulationProcessor pop = new PopulationProcessor(
                List.of(
                        new PopulationData("19104", 2000),
                        new PopulationData("19103", 3000)
                )
        );

        assertTrue(pop.getAllZipCodes().contains("19104"));
        assertTrue(pop.getAllZipCodes().contains("19103"));
    }
}
