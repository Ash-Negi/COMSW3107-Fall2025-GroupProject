package processor;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import data.models.ParkingViolation;

public class ParkingProcessor {

    private List<ParkingViolation> violations;

    public ParkingProcessor(List<ParkingViolation> violations){
        this.violations = violations;
    }

    //menu option 2
    public Map<String, Double> getTotalFinesByZip(){
        Map<String, Double> finesByZip = new HashMap<>();

        for (ParkingViolation v : violations) {
            // skip entries with missing zip or non-PA state
            if (v.getZipCode() == null || v.getZipCode().isEmpty()) continue;
            if (!"PA".equals(v.getState())) continue;

            String zip = v.getZipCode();
            double fine = v.getFine();

            // accumulate fines per ZIP
            finesByZip.put(zip, finesByZip.getOrDefault(zip, 0.0) + fine);
        }

        return finesByZip;
    };

    //Menu option 6
    public int getViolationCountInZip(String zip){
        if (zip == null || zip.isEmpty()) return 0;

        int count = 0;
        for (ParkingViolation v : violations) {
            if (zip.equals(v.getZipCode())) {
                count++;
            }
        }
        return count;
    };

    //Menu option 7
    public double getAverageFineInPhiladelphia(){
        double total = 0.0;
        int count = 0;

        for (ParkingViolation v : violations) {
            if (v.getFine() > 0) {  // ignore invalid fines
                total += v.getFine();
                count++;
            }
        }

        if (count == 0) return 0.0;
        return total / count;
    };

}
