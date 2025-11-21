package parkanalysis.data.loaders;
import parkanalysis.data.models.ParkingViolation;
import java.util.List;

public interface ParkingDataLoader {
    List<ParkingViolation> loadParkingData(String filename) throws Exception;
}
