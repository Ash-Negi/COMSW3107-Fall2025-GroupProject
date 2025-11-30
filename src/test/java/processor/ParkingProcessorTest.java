package processor;

import data.models.ParkingViolation;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingProcessorTest {

    @Test
    public void testTotalFinesByZip() {
        List<ParkingViolation> mockData = List.of(
                new ParkingViolation(LocalDateTime.now(), 50.0, "desc", "V1", "PA", "ID1", "19104"),
                new ParkingViolation(LocalDateTime.now(), 30.0, "desc", "V2", "PA", "ID2", "19104"),
                new ParkingViolation(LocalDateTime.now(), 10.0, "desc", "V3", "NJ", "ID3", "19104"), // should be ignored
                new ParkingViolation(LocalDateTime.now(), 20.0, "desc", "V4", "PA", "ID4", null)     // ignored
        );

        ParkingProcessor pp = new ParkingProcessor(mockData);
        Map<String, Double> result = pp.getTotalFinesByZip();

        assertEquals(80.0, result.get("19104"));  // Only PA fines counted
        assertFalse(result.containsKey("NJ"));
    }
}