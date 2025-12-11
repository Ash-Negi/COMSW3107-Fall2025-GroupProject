package processor;

import data.models.Property;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HousingProcessorTest {
    @Test
    public void testAverageMarketValueBasic() {
        List<Property> list = List.of(
                new Property(100000, 1200, "19104"),
                new Property(300000, 1400, "19104")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(200000.0, hp.getAverageMarketValue("19104"));
    }

    @Test
    public void testAverageMarketValueIgnoresInvalid() {
        List<Property> list = List.of(
                new Property(100000, 1200, "19104"),
                new Property(-1, 1500, "19104"),      // invalid → ignore
                new Property(0, 1500, "19104")        // invalid → ignore
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(100000.0, hp.getAverageMarketValue("19104"));
    }

    @Test
    public void testAverageMarketValueWrongZip() {
        List<Property> list = List.of(
                new Property(100000, 1200, "19103")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(0.0, hp.getAverageMarketValue("19104"));
    }

    @Test
    public void testAverageLivableArea() {
        List<Property> list = List.of(
                new Property(200000, 1000, "19104"),
                new Property(300000, 2000, "19104")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(1500.0, hp.getAverageTotalLivableArea("19104"));
    }

    @Test
    public void testAverageLivableAreaIgnoresInvalid() {
        List<Property> list = List.of(
                new Property(200000, -1, "19104"),   // invalid
                new Property(200000, 0, "19104"),    // invalid
                new Property(200000, 1500, "19104")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(1500.0, hp.getAverageTotalLivableArea("19104"));
    }

    @Test
    public void testTotalMarketValue() {
        List<Property> list = List.of(
                new Property(100000, 1200, "19104"),
                new Property(300000, 1200, "19104")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(400000.0, hp.getTotalMarketValue("19104"));
    }

    @Test
    public void testTotalMarketValueIgnoresInvalid() {
        List<Property> list = List.of(
                new Property(100000, 1200, "19104"),
                new Property(-1, 1200, "19104")
        );

        HousingProcessor hp = new HousingProcessor(list);
        assertEquals(100000.0, hp.getTotalMarketValue("19104"));
    }
}
