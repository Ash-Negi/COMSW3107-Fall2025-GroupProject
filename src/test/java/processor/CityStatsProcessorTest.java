package processor;

import data.models.ParkingViolation;
import data.models.PopulationData;
import data.models.Property;
import processor.cache.StatsCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class CityStatsProcessorTest {
    private PopulationProcessor populationProcessor;
    private HousingProcessor housingProcessor;
    private ParkingProcessor parkingProcessor;
    private StatsCache cache;
    private CityStatsProcessor cityStatsProcessor;

    @BeforeEach
    public void resetStatsCache() throws Exception {
        Field instanceField = StatsCache.class.getDeclaredField("cacheINSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    public void testGetTotalPopulation() throws ExecutionException, InterruptedException {
        PopulationData popData1 = new PopulationData("19104", 2000);
        PopulationData popData2 = new PopulationData("19105", 3000);

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData1, popData2));
        HousingProcessor housingProcessor = new HousingProcessor(List.of());
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getTotalPopulation();
        assertEquals(5000, result);
    }

    @Test
    public void testGetFinesPerCapita() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 2000);
        ParkingViolation violation1 = new ParkingViolation(LocalDateTime.now(), 50.0, "Test", "V1", "PA", "ID1", "19104");
        ParkingViolation violation2 = new ParkingViolation(LocalDateTime.now(), 50.0, "Test", "V2", "PA", "ID2", "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of());
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of(violation1, violation2));

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        Map<String, Double> result = cityStatsProcessor.getFinesPerCapita();
        assertTrue(result.containsKey("19104"));
        assertEquals(0.05, result.get("19104"), 0.0001);
    }

    @Test
    public void testGetAverageMarketValueWithValidZip() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property1 = new Property(250000.0, 1000.0, "19104");
        Property property2 = new Property(350000.0, 1200.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property1, property2));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageMarketValue("19104");
        assertEquals(300000, result);
    }

    @Test
    public void testGetAverageMarketValueRoundDown() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.3, 1000.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageMarketValue("19104");
        assertEquals(250000, result);
    }

    @Test
    public void testGetAverageMarketValueRoundUp() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.5, 1000.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageMarketValue("19104");
        assertEquals(250001, result);
    }

    @Test
    public void testGetAverageMarketValueInvalidZip() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.0, 1000.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageMarketValue("99999");
        assertEquals(0, result);
    }

    @Test
    public void testGetAverageTotalLivableAreaValidZip() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property1 = new Property(250000.0, 1750.0, "19104");
        Property property2 = new Property(350000.0, 2250.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property1, property2));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageTotalLivableArea("19104");
        assertEquals(2000, result);
    }

    @Test
    public void testGetAverageTotalLivableAreaValidZipRoundDown() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.0, 1750.3, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageTotalLivableArea("19104");
        assertEquals(1750, result);
    }

    @Test
    public void testGetAverageTotalLivableAreaValidZipRoundUp() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.0, 1750.5, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getAverageTotalLivableArea("19104");
        assertEquals(1751, result);
    }

    @Test
    public void testGetAverageTotalLivableAreaInvalidZip() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 1000);
        Property property = new Property(250000.0, 1750.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        assertEquals(0, cityStatsProcessor.getAverageTotalLivableArea(null));
        assertEquals(0, cityStatsProcessor.getAverageTotalLivableArea("123"));
        assertEquals(0, cityStatsProcessor.getAverageTotalLivableArea("ABCDE"));
    }

    @Test
    public void testGetMarketValuePerCapitaValidZip() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 2000);
        Property property1 = new Property(600000.0, 1500.0, "19104");
        Property property2 = new Property(400000.0, 1200.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property1, property2));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getMarketValuePerCapita("19104");
        assertEquals(500, result);
    }

    @Test
    public void testGetMarketValuePerCapitaWithZeroPopulation() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 0);
        Property property = new Property(600000.0, 1500.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getMarketValuePerCapita("19104");
        assertEquals(0, result);
    }

    @Test
    public void testGetMarketValuePerCapitaRoundDown() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 3);
        Property property = new Property(1000.0, 1500.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getMarketValuePerCapita("19104");
        assertEquals(333, result);
    }

    @Test
    public void testGetMarketValuePerCapitaZipWithNoPopulation() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 3);
        Property property = new Property(1000.0, 1500.0, "19105");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getMarketValuePerCapita("19105");
        assertEquals(0, result);
    }

    @Test
    public void testGetMarketValuePerCapitaRoundUp() throws ExecutionException, InterruptedException {
        PopulationData popData = new PopulationData("19104", 3);
        Property property = new Property(1001.0, 1500.0, "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of(popData));
        HousingProcessor housingProcessor = new HousingProcessor(List.of(property));
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of());

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        int result = cityStatsProcessor.getMarketValuePerCapita("19104");
        assertEquals(334, result);
    }

    @Test
    public void testViolationsPerCapitaValidZip() throws Exception {
        PopulationData popData = new PopulationData("19104", 2000);
        ParkingViolation violation1 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V1", "PA", "ID1", "19104");
        ParkingViolation violation2 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V2", "PA", "ID2", "19104");

        PopulationProcessor pop = new PopulationProcessor(List.of(popData));
        HousingProcessor housing = new HousingProcessor(List.of());
        ParkingProcessor parking = new ParkingProcessor(List.of(violation1, violation2));

        StatsCache.initializeCacheBuild(housing, parking, pop);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor stats = new CityStatsProcessor(pop, housing, parking, cache);

        assertEquals(0.001, stats.getViolationsPerCapita("19104"), 0.00001);
    }

    @Test
    public void testViolationsPerCapitaInvalidZip() throws Exception {
        PopulationData popData = new PopulationData("19104", 2000);
        ParkingViolation violation1 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V1", "PA", "ID1", "19104");

        PopulationProcessor pop = new PopulationProcessor(List.of(popData));
        HousingProcessor housing = new HousingProcessor(List.of());
        ParkingProcessor parking = new ParkingProcessor(List.of(violation1));

        StatsCache.initializeCacheBuild(housing, parking, pop);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor stats = new CityStatsProcessor(pop, housing, parking, cache);

        assertEquals(0.0, stats.getViolationsPerCapita("BADZIP"), 0.001);
    }

    @Test
    public void testGetAverageFineInPhiladelphia() throws ExecutionException, InterruptedException {
        ParkingViolation violation1 = new ParkingViolation(LocalDateTime.now(), 50.0, "Test1", "V1", "PA", "ID1", "19104");
        ParkingViolation violation2 = new ParkingViolation(LocalDateTime.now(), 100.0, "Test2", "V2", "PA", "ID2", "19105");
        ParkingViolation violation3 = new ParkingViolation(LocalDateTime.now(), 0.0, "Test3", "V3", "PA", "ID3", "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of());
        HousingProcessor housingProcessor = new HousingProcessor(List.of());
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of(violation1, violation2, violation3));

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        double result = cityStatsProcessor.getAverageFineInPhiladelphia();
        assertEquals(75.0, result, 0.0001);
    }

    @Test
    public void testGetAverageFineInPhiladelphiaNoValidFines() throws ExecutionException, InterruptedException {
        ParkingViolation violation = new ParkingViolation(LocalDateTime.now(), 0.0, "Test", "V1", "PA", "ID1", "19104");

        PopulationProcessor populationProcessor = new PopulationProcessor(List.of());
        HousingProcessor housingProcessor = new HousingProcessor(List.of());
        ParkingProcessor parkingProcessor = new ParkingProcessor(List.of(violation));

        StatsCache.initializeCacheBuild(housingProcessor, parkingProcessor, populationProcessor);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor cityStatsProcessor = new CityStatsProcessor(populationProcessor, housingProcessor, parkingProcessor, cache);

        double result = cityStatsProcessor.getAverageFineInPhiladelphia();
        assertEquals(0.0, result, 0.0001);
    }

    /*
    @Test
    public void testViolationsPerCapita() throws ExecutionException, InterruptedException {
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

        StatsCache.initializeCacheBuild(housing, parking, pop);
        StatsCache cache = StatsCache.getInstance();

        CityStatsProcessor stats = new CityStatsProcessor(pop, housing, parking, cache);

        assertEquals(0.001, stats.getViolationsPerCapita("19104"), 0.00001);
        assertEquals(0.0, stats.getViolationsPerCapita("BADZIP"), 0.001);
    }
     */
}