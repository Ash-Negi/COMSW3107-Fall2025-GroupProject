package data;
import data.loaders.*;
import data.models.ParkingViolation;
import data.models.Property;
import data.models.PopulationData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataManager {
    private List<ParkingViolation> parkingViolations;
    private List<Property> properties;
    private Map<String, Integer> populationMap;

    public void loadAllData(String format, String parkingFile, String propertyFile, String populationFile) throws Exception{
        System.out.println("Loading data files.");

        // load parking data based on csv or json
        System.out.println("Loading parking violations from " + parkingFile + " (" + format + " format)");
        ParkingDataLoader parkingLoader;
        if(format.equals("csv")){
            parkingLoader = new CSVParkingLoader();
        }
        else{
            parkingLoader = new JSONParkingLoader();
        }
        this.parkingViolations = parkingLoader.loadParkingData(parkingFile);
        System.out.println("Loaded " + parkingViolations.size() + " parking violations");

        // load parking data
        System.out.println("Loading property data from " + propertyFile);
        PropertyDataLoader propertyLoader = new PropertyDataLoader();
        this.properties = propertyLoader.loadPropertyData(propertyFile);
        System.out.println("Loaded " + properties.size() + " properties");

        // load population data
        System.out.println("Loading population data from " + populationFile);
        PopulationDataLoader populationLoader = new PopulationDataLoader();
        List<PopulationData> populationList = populationLoader.loadPopulationData((populationFile));
        this.populationMap = populationList.stream().collect(Collectors.toMap(PopulationData::getZipCode, PopulationData::getPopulation));
        System.out.println("Loaded population data for " + populationMap.size() + " zip codes");

        System.out.println("Data successfully loaded.");
    }

    // getter methods for other tiers to access data
    public List<ParkingViolation> getParkingViolations(){
        return parkingViolations;
    }
    public List<Property> getProperties(){
        return properties;
    }
    public Map<String, Integer> getPopulationMap(){
        return populationMap;
    }

    // helper method to get properties by zip code
    public List<Property> getPropertiesByZipCode(String zipCode){
        return properties.stream().filter(property -> zipCode.equals(property.getZipCode())).collect(Collectors.toList());
    }

    // helper method to get parking violations by zip code
    public List<ParkingViolation> getParkingViolationsByZipCode(String zipCode){
        return parkingViolations.stream().filter(violation -> zipCode.equals(violation.getZipCode())).collect(Collectors.toList());
    }
}
