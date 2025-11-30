package data.loaders;
import data.models.PopulationData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PopulationDataLoader {
    public List<PopulationData> loadPopulationData(String filename) throws Exception{
        List<PopulationData> populationData = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                try{
                    // split by whitespace
                    String[] parts = line.trim().split("\\s+");
                    if(parts.length >= 2){
                        String zipCode = parts[0].trim();
                        int population = Integer.parseInt(parts[1].trim());
                        populationData.add(new PopulationData(zipCode, population));
                    }
                }
                catch(Exception e){
                    // skip invalid lines + continue processing
                    System.err.println("Warning: Skipping invalid population line: " + line);
                }
            }
        }
        return populationData;
    }
}
