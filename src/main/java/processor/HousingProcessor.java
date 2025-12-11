package processor;

import data.models.PopulationData;
import data.models.Property;
import data.models.ParkingViolation;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HousingProcessor {

    private List<Property> properties;

    public HousingProcessor(List<Property> properties){
        this.properties  = properties;
    }

    //option 3
    public double getAverageMarketValue(String zip){
//        double total = 0.0;
//        int count = 0;
//
//        for (Property p : properties) {
//            if (p.getZipCode() != null && p.getZipCode().equals(zip)) {
//                double value = p.getMarketValue();
//                if (value > 0) { // ignore invalid
//                    total += value;
//                    count++;
//                }
//            }
//        }
//
//        if (count == 0) return 0.0;
//        return total / count;
        if (zip == null) return 0;

        double total = 0;
        int count = 0;

        for (Property p : properties) {
            if (zip.equals(p.getZipCode())) {
                total += p.getMarketValue();
                count++;
            }
        }

        System.err.println("FINISHED ZIP=" + zip + "  total=" + total + " count=" + count);

        if (count == 0) return 0;
        return total / count;
    };

    //option 4
    public double getAverageTotalLivableArea(String zip){
        double total = 0.0;
        int count = 0;

        for (Property p : properties) {
            if (p.getZipCode() != null && p.getZipCode().equals(zip)) {
                double area = p.getTotalLivableArea();
                if (area > 0) {
                    total += area;
                    count++;
                }
            }
        }

        if (count == 0) return 0.0;
        return total / count;
    };

    //option 5
    public double getTotalMarketValue(String zip){
        double total = 0.0;

        for (Property p : properties) {
            if (p.getZipCode() != null && p.getZipCode().equals(zip)) {
                double value = p.getMarketValue();
                if (value > 0) {
                    total += value;
                }
            }
        }

        return total;
    };

    public int getHouseCount(String zip){
        int count = 0;

        for (Property p : properties) {
            if (p.getZipCode() != null && p.getZipCode().equals(zip)) {
                count++;
            }
        }

        return count;
    };

    public Set<String> getAllZipCodes() {
        return properties.stream()
                .map(Property::getZipCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
