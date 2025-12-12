package data.loaders;
import data.models.Property;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PropertyDataLoader {
    public List<Property> loadPropertyData(String filename) throws Exception{
        List<Property> properties = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            // read header row to find column indices
            String headerLine = br.readLine();
            if(headerLine == null){
                throw new IllegalArgumentException("Property file is empty");
            }

            String[] headers = headerLine.split(",");
            int marketValueIndex = -1;
            int livableAreaIndex = -1;
            int zipCodeIndex = -1;

            // find indices for fields we want
            for(int i = 0; i < headers.length; i++){
                String header = headers[i].trim();
                switch(header){
                    case "market_value":
                        marketValueIndex = i;
                        break;
                    case "total_livable_area":
                        livableAreaIndex = i;
                        break;
                    case "zip_code":
                        zipCodeIndex = i;
                        break;
                }
            }

            // make sure all fields were found
            if(marketValueIndex == -1 || livableAreaIndex == -1 || zipCodeIndex == -1){
                throw new IllegalArgumentException("Missing required fields in property file header");
            }

            // process data rows
            String line;
            while((line = br.readLine()) != null){
                try{
                    Property property = parsePropertyLine(line, marketValueIndex, livableAreaIndex, zipCodeIndex);
                    if(property != null){
                        properties.add(property);
                    }
                }
                catch(Exception e){
                    // skip invalid lines + continue processing
                    System.err.println("Warning: Skipping invalid property line: " + line);
                }
            }
        }
        return properties;
    }

    private Property parsePropertyLine(String line, int marketValueIndex, int livableAreaIndex, int zipCodeIndex) {
        //instead of returning null when number is invalid
        //we'll assign a flag value so the valid parts of the row are saved
        String[] fields = line.split(",",-1);

        //Check if the number of columns are correct
        if(fields.length <= Math.max(marketValueIndex, Math.max(livableAreaIndex, zipCodeIndex))){
            return null;
        }

        // 1. this is the parameter we return null for if its bad
        String zipCode = fields[zipCodeIndex].trim();
        // Spec says to strictly parse first 5 digits
        if (zipCode.length() > 5) zipCode = zipCode.substring(0, 5);

        if (zipCode.isEmpty() || !zipCode.matches("\\d{5}")) { // Validate 5 digits
            return null;
        }

        // market value, changed functionality to set to -1.0 if invalid, not returning null)
        double marketValue = -1.0;
        try {
            String str = fields[marketValueIndex].trim();
            if (!str.isEmpty()) {
                double val = Double.parseDouble(str);
                if (val > 0) marketValue = val; // Only accept positive
            }
        } catch (Exception ignored) {
            // Keeps marketValue as -1.0
        }
        // 3. setting to -1.0 if invalid, not returning null
        double livableArea = -1.0;
        try {
            String str = fields[livableAreaIndex].trim();
            if (!str.isEmpty()) {
                double val = Double.parseDouble(str);
                if (val > 0) livableArea = val; // Only accept positive
            }
        } catch (Exception ignored) {
            // Keeps livableArea as -1.0
        }

        // Return the property even if marketValue or livableArea is flagged -1.0
        return new Property(marketValue, livableArea, zipCode);
    }
}
