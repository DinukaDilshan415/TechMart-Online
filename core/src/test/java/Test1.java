import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ArquillianTest
public class Test1 {
    @Test
    void calculateDiscountTest() {

        double result = calculateDiscount(1000,10);

        assertEquals(900,result);

    }

    public double calculateDiscount(double price, int discount){

        return price - (price * discount / 100.0);

    }
}
