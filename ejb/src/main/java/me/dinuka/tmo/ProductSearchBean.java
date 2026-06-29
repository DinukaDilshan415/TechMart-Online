package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import me.dinuka.tmo.dto.CategoriesDTO;
import me.dinuka.tmo.dto.SearchDetailsDTO;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.service.ProductSearchService;
import me.dinuka.tmo.util.RegexValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Stateless
public class ProductSearchBean implements ProductSearchService {

    final int ACTIVE_STATUS_ID = 1;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public String searchProducts(Map<String, Object> requestData) {
        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.toJsonTree(requestData).getAsJsonObject();

        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        JsonArray brands = requestJsonObject.get("brands").getAsJsonArray();
        String mainCategoryId = requestJsonObject.get("mainCategoryId").getAsString();
        String categoryId = requestJsonObject.get("categoryId").getAsString();
        String subCategoryId = requestJsonObject.get("subCategoryId").getAsString();
        String maxPrice = requestJsonObject.get("maxPrice").getAsString();
        String minPrice = requestJsonObject.get("minPrice").getAsString();
        String sortBy = requestJsonObject.get("sortBy").getAsString();

        List<Brand> brandList = StreamSupport.stream(brands.spliterator(), false)
                .map(e -> {
                    JsonObject obj = e.getAsJsonObject();
                    int id = obj.get("id").getAsInt();
                    String name = obj.get("name").getAsString();
                    return new Brand(id, name);
                })
                .collect(Collectors.toList());

        // BUILD A SINGLE UNIFIED QUERY WITH ALL FILTERS
        StringBuilder productJpql = new StringBuilder("SELECT p FROM Product p WHERE 1=1 ");

        boolean hasFindText = requestJsonObject.has("findText");
        boolean hasBrands = brandList != null && !brandList.isEmpty();

        // Add findText filter
        if (hasFindText) {
            productJpql.append("AND LOWER(p.title) LIKE LOWER(:findText) ");
            productJpql.append("AND p.status = :status ");
        }

        // Add brands filter
        if (hasBrands) {
            productJpql.append("AND p.brand IN :brands ");
        }

        // Add category filters
        if (!mainCategoryId.isEmpty()) {
            if (!categoryId.isEmpty()) {
                if (!subCategoryId.isEmpty()) {
                    productJpql.append("AND p.subCategory.id = :subCategoryId ");
                } else {
                    productJpql.append("AND p.subCategory.category.id = :categoryId ");
                }
            } else {
                productJpql.append("AND p.subCategory.category.mainCategory.id = :mainCategoryId ");
            }
        }

        TypedQuery<Product> productQuery = em.createQuery(productJpql.toString(), Product.class);

        // Set all parameters
        if (hasFindText) {
            String findText = requestJsonObject.get("findText").getAsString();
            Status status = em.find(Status.class, ACTIVE_STATUS_ID);

            productQuery.setParameter("findText", "%" + findText + "%");
            productQuery.setParameter("status", status);
        }

        if (hasBrands) {
            productQuery.setParameter("brands", brandList);
        }

        if (!mainCategoryId.isEmpty()) {
            if (!categoryId.isEmpty()) {
                if (!subCategoryId.isEmpty()) {
                    productQuery.setParameter("subCategoryId", Double.valueOf(subCategoryId).intValue());
                } else {
                    productQuery.setParameter("categoryId", Double.valueOf(categoryId).intValue());
                }
            } else {
                productQuery.setParameter("mainCategoryId", Double.valueOf(mainCategoryId).intValue());
            }
        }

        List<Product> productList = productQuery.getResultList();

        List<Stock> stockList = new ArrayList<>();

        if (!productList.isEmpty()) {
            StringBuilder stockJpql = new StringBuilder("SELECT s FROM Stock s WHERE s.product IN :products ");

            boolean hasValidPriceRange = minPrice != null && maxPrice != null
                    && RegexValidator.isValidAmount(minPrice) && RegexValidator.isValidAmount(maxPrice);

            if (hasValidPriceRange) {
                stockJpql.append("AND s.price >= :minPrice AND s.price <= :maxPrice ");
            }

            switch (sortBy) {
                case "discount-desc" -> stockJpql.append("ORDER BY s.product.discount DESC");
                case "name-asc"       -> stockJpql.append("ORDER BY s.product.title ASC");
                case "name-desc"      -> stockJpql.append("ORDER BY s.product.title DESC");
                case "price-asc"      -> stockJpql.append("ORDER BY s.price ASC");
                case "price-desc"     -> stockJpql.append("ORDER BY s.price DESC");
            }

            TypedQuery<Stock> stockQuery = em.createQuery(stockJpql.toString(), Stock.class);
            stockQuery.setParameter("products", productList);

            if (hasValidPriceRange) {
                stockQuery.setParameter("minPrice", Double.parseDouble(minPrice));
                stockQuery.setParameter("maxPrice", Double.parseDouble(maxPrice));
            }

            stockList = stockQuery.getResultList();

            resposeObject.add("productList", gson.toJsonTree(productList));
            resposeObject.add("stockList", gson.toJsonTree(stockList));
            resposeObject.addProperty("status", true);
        } else {
            resposeObject.addProperty("message", "Products Not Found !");
        }

        return gson.toJson(resposeObject);
    }

    @Override
    public String loadSearchDetails() {
        Gson gson = new Gson();
        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        List<SearchDetailsDTO> searchDetailsList = new ArrayList<>();

        String mainCategoryJpql = "SELECT mc FROM MainCategory mc";
        List<MainCategory> mainCategoryList = em.createQuery(mainCategoryJpql, MainCategory.class)
                .getResultList();

        String categoryJpql = "SELECT c FROM Category c";
        List<Category> allCategories = em.createQuery(categoryJpql, Category.class)
                .getResultList();

        String subCategoryJpql = "SELECT sc FROM SubCategory sc";
        List<SubCategory> allSubCategories = em.createQuery(subCategoryJpql, SubCategory.class)
                .getResultList();

        for (MainCategory mainCategory : mainCategoryList) {
            SearchDetailsDTO searchDetails = new SearchDetailsDTO();
            searchDetails.setMainCategory(mainCategory);

            List<Category> categoryList = allCategories.stream()
                    .filter(c -> c.getMainCategory().getId() == mainCategory.getId())
                    .toList();

            ArrayList<CategoriesDTO> cList = new ArrayList<>();
            for (Category category : categoryList) {

                List<SubCategory> subCategoryList = allSubCategories.stream()
                        .filter(sc -> sc.getCategory().getId() == category.getId())
                        .toList();

                CategoriesDTO sc = new CategoriesDTO();
                sc.setCategory(category);
                sc.setSubCategories(subCategoryList);

                cList.add(sc);
            }
            searchDetails.setCategories(cList);
            searchDetailsList.add(searchDetails);
        }

        String brandJpql = "SELECT b FROM Brand b";
        List<Brand> brandList = em.createQuery(brandJpql, Brand.class)
                .getResultList();
        resposeObject.add("brandList", gson.toJsonTree(brandList));


        resposeObject.addProperty("status", true);
        resposeObject.add("searchDetails", gson.toJsonTree(searchDetailsList));

        return gson.toJson(resposeObject);
    }
}
