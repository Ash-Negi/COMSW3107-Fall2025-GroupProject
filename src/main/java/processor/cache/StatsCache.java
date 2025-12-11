package processor.cache;

import processor.HousingProcessor;
import processor.ParkingProcessor;
import processor.PopulationProcessor;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;

/*
* Holds precomputed statistics in order to allow fast lookups
* Created once when the program is first ran
* */

public class StatsCache {

    private static StatsCache cacheINSTANCE;

    private StatsCache() { }

    public static StatsCache getInstance(){
        if(cacheINSTANCE == null){
            throw new IllegalStateException(
                    "StatsCache not yet initialized. Use StatsCache.initializeCacheBuild() to create cache"
            );
        }
        return cacheINSTANCE;
    }

    //we only allow one instance
    public static void initializeCacheBuild(HousingProcessor housing,
                                  ParkingProcessor parking,
                                  PopulationProcessor population)
            throws ExecutionException, InterruptedException {

        if (cacheINSTANCE != null){
            throw new IllegalStateException("StatsCache already initialized");
        }
        cacheINSTANCE = new StatsCacheBuilder().buildCache(housing, parking, population);
    };

    //housing stats
    private final Map<String, Double> avgMarketValueByZip = new TreeMap<>();
    private final Map<String, Double> avgLivableAreaByZip = new TreeMap<>();
    private final Map<String, Double> totalMarketValueByZip = new TreeMap<>();

    // Parking/population dependent stats
    private final Map<String, Double> finesPerCapitaByZip = new TreeMap<>();
    private final Map<String, Double> violationsPerCapitaByZip = new TreeMap<>();

    // Getter methods, other parts of the program will use these to retrieve data
    public Map<String, Double> getAvgMarketValueByZip() { return avgMarketValueByZip; }
    public Map<String, Double> getAvgLivableAreaByZip() { return avgLivableAreaByZip; }
    public Map<String, Double> getTotalMarketValueByZip() { return totalMarketValueByZip; }

    public Map<String, Double> getFinesPerCapitaByZip() { return finesPerCapitaByZip; }
    public Map<String, Double> getViolationsPerCapitaByZip() { return violationsPerCapitaByZip; }

    public static class StatsCacheBuilder {

        private ExecutorService es = Executors.newFixedThreadPool(3);

        public StatsCache buildCache(
                HousingProcessor housing,
                ParkingProcessor parking,
                PopulationProcessor population
        ) throws InterruptedException, ExecutionException {

            StatsCache cache = new StatsCache();

            //Compute housing statistics Callable
            Callable<Void> housingTask = () -> {
                System.err.println("HOUSING TASK STARTED");

                System.err.println("Housing ZIP codes: " + housing.getAllZipCodes());
                for(String zip : housing.getAllZipCodes()) {
                    double avgMarketVal = housing.getAverageMarketValue(zip);
                    double avgTotalLivableArea = housing.getAverageTotalLivableArea(zip);
                    double totalMktVal = housing.getTotalMarketValue(zip);

                    System.err.println("  avgMarketVal=" + avgMarketVal +
                            " avgLivableArea=" + avgTotalLivableArea +
                            " totalMktVal=" + totalMktVal);

                    if (avgMarketVal > 0) cache.getAvgMarketValueByZip().put(zip, avgMarketVal);
                    if (avgTotalLivableArea > 0)   cache.getAvgLivableAreaByZip().put(zip, avgTotalLivableArea);
                    if (totalMktVal > 0)   cache.getTotalMarketValueByZip().put(zip, totalMktVal);
                }
                System.err.println("HOUSING TASK FINISHED");
                return null;
            };

            //just a note, threads below don't write to the same maps
            //but TreeMap objects themselves aren't thread safe though if multiple threads were to write to the same map

            //compute fines per capita Callable
            Callable<Void> finesTask = () -> {
                Map<String, Double> fines = parking.getTotalFinesByZip();
                for (String zip : fines.keySet()) {
                    int pop = population.getPopulationForZip(zip);
                    if (pop > 0) {
                        cache.getFinesPerCapitaByZip().put(zip, fines.get(zip) / pop);
                    }
                }
                return null;
            };

            //compute violations per capita Callable
            Callable<Void> violationsTask = () -> {
                for (String zip : population.getAllZipCodes()) {
                    int pop = population.getPopulationForZip(zip);
                    if (pop > 0) {
                        double perCap = (double) parking.getViolationCountInZip(zip) / pop;
                        cache.getViolationsPerCapitaByZip().put(zip, perCap);
                    }
                }
                return null;
            };

            //run tasks concurrently
            Future<Void> f1 = es.submit(housingTask);
            Future<Void> f2 = es.submit(finesTask);
            Future<Void> f3 = es.submit(violationsTask);

            // Wait for completion
            try {
                f1.get();
                f2.get();
                f3.get();
            } finally {
                es.shutdown();
            }

            return cache;

        }

    }

}
