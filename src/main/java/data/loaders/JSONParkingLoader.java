package data.loaders;
import data.models.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JSONParkingLoader implements ParkingDataLoader{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public List<ParkingViolation> loadParkingData(String filename) throws Exception{
        List<ParkingViolation> violations = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try(FileReader reader = new FileReader(filename)){
            // parse the JSON file
            Object obj = parser.parse(reader);
            JSONArray violationsArray = (JSONArray) obj;

            // process each violation
            for(Object violationObj : violationsArray){
                try{
                    ParkingViolation violation = parseJSONObject((JSONObject) violationObj);
                    if(violation != null){
                        violations.add(violation);
                    }
                }
                catch(Exception e){
                    // skip invalid objects + continue processing
                    System.err.println("Warning: Skipping invalid JSON object");
                }
            }
        }
        return violations;
    }

    private ParkingViolation parseJSONObject(JSONObject jsonObject){
        try{
            // parse timestamp
            String timestampStr = (String) jsonObject.get("timestamp");
            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DATE_FORMATTER);

            // parse fine
            double fine;
            Object fineObj = jsonObject.get("fine");
            if(fineObj instanceof Number){
                fine = ((Number) fineObj).doubleValue();
            }
            else{
                fine = Double.parseDouble((String) fineObj);
            }

            // extract other fields
            String description = (String) jsonObject.get("description");
            String vehicleId = (String) jsonObject.get("vehicleId");
            String state = (String) jsonObject.get("state");
            String violationId = (String) jsonObject.get("violationId");

            // handle zip code, might be null
            String zipCode = (String) jsonObject.get("zipCode");
            if(zipCode != null && zipCode.isEmpty()){
                zipCode = null;
            }

            return new ParkingViolation(timestamp, fine, description, vehicleId, state, violationId, zipCode);

        }
        catch(Exception e){
            return null;
        }
    }

}
