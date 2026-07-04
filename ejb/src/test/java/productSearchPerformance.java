import me.dinuka.tmo.ProductSearchBean;
import me.dinuka.tmo.service.ProductSearchService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class productSearchPerformance {

    @Test
    void productSearchPerformance(){

        ProductSearchService productService = new ProductSearchBean();

        long start = System.currentTimeMillis();

        Map<String, Object> searchDetails = new HashMap<>();
        searchDetails.put("brands", new ArrayList<>());
        searchDetails.put("categoryId", "");
        searchDetails.put("findText", "");
        searchDetails.put("mainCategoryId", "");
        searchDetails.put("maxPrice", 1000000);
        searchDetails.put("minPrice", 0);
        searchDetails.put("sortBy", "default");
        searchDetails.put("subCategoryId", "");

        productService.searchProducts(searchDetails);

        long end = System.currentTimeMillis();

        long time = end-start;

        assertTrue(time < 500);

    }

}
