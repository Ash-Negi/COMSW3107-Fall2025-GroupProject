package processor;

import data.models.Property;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HousingProcessorTest {
    @Test
    public void testAverageMarketValue() {
        List<Property> properties = List.of(
                new Property(100000, 1200, "19104"),
                new Property(200000, 1300, "19104"),
                new Property(-1, 900, "19104"),      // invalid → must ignore
                new Property(500000, 1500, "19103")  // different ZIP
        );

        HousingProcessor hp = new HousingProcessor(properties);

        assertEquals(150000.0, hp.getAverageMarketValue("19104"), 0.01);
        assertEquals(500000.0, hp.getAverageMarketValue("19103"), 0.01);
        assertEquals(0.0, hp.getAverageMarketValue("00000"), 0.01);
    }
}
