package data.loaders;
import data.models.ParkingViolation;
import java.util.List;

public interface ParkingDataLoader {
    List<ParkingViolation> loadParkingData(String filename) throws Exception;
}
