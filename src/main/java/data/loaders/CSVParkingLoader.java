package parkanalysis.data.loaders;

import parkanalysis.data.models.ParkingViolation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVParkingLoader implements ParkingDataLoader{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public List<ParkingViolation> loadParkingData(String filename) throws Exception{
        List<ParkingViolation> violations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null){
                try{
                    ParkingViolation violation = parseCSVLine(line);
                    if (violation != null){
                        violations.add(violation);
                    }
                }
                catch (Exception e){
                    System.err.println("Warning: Skipping invalid line: " + line);
                }
            }
        }
        return violations;
    }

    private ParkingViolation parseCSVLine(String line){
        String[] fields = line.split(",");

        if (fields.length != 7){
            return null;
        }

        try{
            // parse timestamp
            LocalDateTime timestamp = LocalDateTime.parse(fields[0], DATE_FORMATTER);

            // parse fine
            double fine = Double.parseDouble(fields[1]);

            String description = fields[2].trim();
            String vehicleId = fields[3].trim();
            String state = fields[4].trim();
            String violationId = fields[5].trim();
            String zipCode = fields[6].trim();

            // empty zip codes
            if (zipCode.isEmpty()){
                zipCode = null;
            }

            return new ParkingViolation(timestamp, fine, description, vehicleId, state, violationId, zipCode);

        }
        catch (Exception e){
            return null;
        }
    }

}
