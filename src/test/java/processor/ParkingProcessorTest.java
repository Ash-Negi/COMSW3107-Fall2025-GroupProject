package processor;

import data.models.ParkingViolation;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingProcessorTest {

    @Test
    public void testViolationCountInZip() {
        ParkingViolation v1 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V1", "PA", "ID1", "19104");
        ParkingViolation v2 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V2", "PA", "ID2", "19104");
        ParkingViolation v3 = new ParkingViolation(LocalDateTime.now(), 50, "desc", "V3", "NJ", "ID3", "19104"); // ignored

        ParkingProcessor p = new ParkingProcessor(List.of(v1, v2, v3));

        assertEquals(2, p.getViolationCountInZip("19104"));
    }

    @Test
    public void testTotalFinesByZip() {
        ParkingViolation v1 = new ParkingViolation(LocalDateTime.now(), 40, "d", "V1", "PA", "ID1", "19104");
        ParkingViolation v2 = new ParkingViolation(LocalDateTime.now(), 60, "d", "V2", "PA", "ID2", "19104");

        ParkingProcessor proc = new ParkingProcessor(List.of(v1, v2));

        Map<String, Double> result = proc.getTotalFinesByZip();
        assertEquals(100.0, result.get("19104"));
    }

    @Test
    public void testTotalFinesIgnoresInvalidZip() {
        ParkingViolation v1 = new ParkingViolation(LocalDateTime.now(), 40, "d", "V1", "PA", "ID1", "");
        ParkingViolation v2 = new ParkingViolation(LocalDateTime.now(), 60, "d", "V2", "PA", "ID2", null);

        ParkingProcessor proc = new ParkingProcessor(List.of(v1, v2));

        Map<String, Double> result = proc.getTotalFinesByZip();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAverageFine() {
        ParkingViolation v1 = new ParkingViolation(LocalDateTime.now(), 50, "d", "V1", "PA", "ID1", "19104");
        ParkingViolation v2 = new ParkingViolation(LocalDateTime.now(), 100, "d", "V2", "PA", "ID2", "19104");

        ParkingProcessor proc = new ParkingProcessor(List.of(v1, v2));

        assertEquals(75.0, proc.getAverageFineInPhiladelphia());
    }

    @Test
    public void testAverageFineNoValidEntries() {
        ParkingViolation v1 = new ParkingViolation(LocalDateTime.now(), 0, "d", "V1", "PA", "ID1", "19104");

        ParkingProcessor proc = new ParkingProcessor(List.of(v1));

        assertEquals(0.0, proc.getAverageFineInPhiladelphia());
    }
}