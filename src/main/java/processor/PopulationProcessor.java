package processor;

import data.PopulationData;
import data.Property;
import data.ParkingViolation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationProcessor {

    private Map<String, Integer> populationByZip;

    public PopulationProcessor(List<PopulationData> populationRecords){

        populationByZip = new HashMap<>();

        if(populationRecords == null){
            return;
        }

        for(PopulationData record: populationRecords){
            if(record != null && record.getZipCode() != null){
                 populationByZip.put(record.getZipCode(), record.getPopulation());
            }
        }
    }

    //option 1
    public int getTotalPopulation(){

        int total = 0;
        for(Integer population: populationByZip.values()){
            if(population != null && population >= 0){
                total += population;
            }
        }
        return total;
    };

    //helper method to get population for specific zip
    public int getPopulationForZip(String zip){
        if(zip == null || !populationByZip.containsKey(zip)){
            return 0;
        }
        return populationByZip.get(zip);
    }
}
