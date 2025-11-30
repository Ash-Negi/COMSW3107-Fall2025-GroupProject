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

    private Property parsePropertyLine(String line, int marketValueIndex, int livableAreaIndex, int zipCodeIndex){
        String[] fields = line.split(",");

        if(fields.length <= Math.max(marketValueIndex, Math.max(livableAreaIndex, zipCodeIndex))){
            return null;
        }

        try{
            // parse market value. skip if empty or invalid
            String marketValueStr = fields[marketValueIndex].trim();
            if(marketValueStr.isEmpty()){
                return null;
            }
            double marketValue = Double.parseDouble(marketValueStr);
            if(marketValue <= 0){
                return null;
            }

            // parse total livable area. skip if empty or invalid
            String livableAreaStr = fields[livableAreaIndex].trim();
            if(livableAreaStr.isEmpty()){
                return null;
            }
            double livableArea = Double.parseDouble(livableAreaStr);
            if(livableArea <= 0){
                return null;
            }

            // parse zip code. take first 5 digits
            String zipCode = fields[zipCodeIndex].trim();
            if(zipCode.isEmpty()){
                return null;
            }
            if(zipCode.length() > 5){
                zipCode = zipCode.substring(0, 5);
            }

            return new Property(marketValue, livableArea, zipCode);
        }
        catch(NumberFormatException e){
            return null;
        }
        catch(Exception e){
            return null;
        }
    }
}
