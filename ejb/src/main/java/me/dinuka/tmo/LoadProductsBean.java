package me.dinuka.tmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.HomeProductsDTO;
import me.dinuka.tmo.dto.ProductDTO;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.service.ProductLoadService;
import me.dinuka.tmo.util.RegexValidator;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class LoadProductsBean implements ProductLoadService {

    final int ACTIVE_STATUS_ID = 1;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public List<HomeProductsDTO> loadAllProducts() {
        List<HomeProductsDTO> homeProductsList = new ArrayList<>();

        Status status = em.find(Status.class, ACTIVE_STATUS_ID);

        List<MainCategory> mainCategoryList = em.createQuery(
                        "SELECT mc FROM MainCategory mc", MainCategory.class)
                .getResultList();

        for (MainCategory mainCategory : mainCategoryList) {

            HomeProductsDTO homeProducts = new HomeProductsDTO();
            homeProducts.setMainCategory(mainCategory);

            List<Category> categoryList = em.createQuery(
                            "SELECT c FROM Category c WHERE c.mainCategory = :mainCategory", Category.class)
                    .setParameter("mainCategory", mainCategory)
                    .getResultList();

            homeProducts.setCategorys(categoryList);

            if (!categoryList.isEmpty()) {
                List<SubCategory> subCategoryList = em.createQuery(
                                "SELECT sc FROM SubCategory sc WHERE sc.category IN :categories", SubCategory.class)
                        .setParameter("categories", categoryList)
                        .getResultList();

                if (!subCategoryList.isEmpty()) {
                    List<Product> productList = em.createQuery(
                                    "SELECT p FROM Product p WHERE p.status = :status AND p.subCategory IN :subCategories", Product.class)
                            .setParameter("status", status)
                            .setParameter("subCategories", subCategoryList)
                            .getResultList();

                    if (!productList.isEmpty()) {
                        List<Stock> stockList = em.createQuery(
                                        "SELECT s FROM Stock s WHERE s.product IN :products", Stock.class)
                                .setParameter("products", productList)
                                .getResultList();

                        homeProducts.setProducts(productList);
                        homeProducts.setStocks(stockList);

                        homeProductsList.add(homeProducts);
                    }
                }
            }
        }

        return homeProductsList;
    }


    @Override
    public String getProductById(String id) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (RegexValidator.isValidInteger(id)) {
            try {
                Product product = em.find(Product.class, Integer.valueOf(id));

                if (product != null && product.getStatus().getValue().equals("Active")) {
                    String jpql = "SELECT s FROM Stock s WHERE s.product = :product";
                    List<Stock> stocks = em.createQuery(jpql, Stock.class)
                            .setParameter("product", product)
                            .getResultList();

                    Stock stock = stocks.isEmpty() ? null : stocks.get(0);

                    MainCategory mainCategory = product.getSubCategory().getCategory().getMainCategory();

                    String categoryJpql = "SELECT c FROM Category c WHERE c.mainCategory = :mainCategory";
                    List<Category> list1 = em.createQuery(categoryJpql, Category.class)
                            .setParameter("mainCategory", mainCategory)
                            .getResultList();

                    List<SubCategory> subCategoryList = new ArrayList<>();
                    if (!list1.isEmpty()) {
                        String subCategoryJpql = "SELECT sc FROM SubCategory sc WHERE sc.category IN :categories";
                        subCategoryList = em.createQuery(subCategoryJpql, SubCategory.class)
                                .setParameter("categories", list1)
                                .getResultList();
                    }

                    List<Product> productList = new ArrayList<>();
                    List<Stock> stockList = new ArrayList<>();

                    if (!subCategoryList.isEmpty()) {
                        Status status = em.find(Status.class, ACTIVE_STATUS_ID);
                        String productJpql = "SELECT p FROM Product p " +
                                "WHERE p.subCategory IN :subCategories " +
                                "AND p.id != :currentId " +
                                "AND p.status = :status";

                        productList = em.createQuery(productJpql, Product.class)
                                .setParameter("subCategories", subCategoryList)
                                .setParameter("currentId", product.getId())
                                .setParameter("status", status)
                                // .setMaxResults(6)
                                .getResultList();

                        if (!productList.isEmpty()) {
                            String stockJpql = "SELECT s FROM Stock s WHERE s.product IN :products";
                            stockList = em.createQuery(stockJpql, Stock.class)
                                    .setParameter("products", productList)
                                    .getResultList();
                        }
                    }

                    ResponseObject.add("product", gson.toJsonTree(product));
                    ResponseObject.add("stock", gson.toJsonTree(stock));
                    ResponseObject.add("productList", gson.toJsonTree(productList));
                    ResponseObject.add("stockList", gson.toJsonTree(stockList));

                    ResponseObject.addProperty("imagePath", "imagePath");
                    ResponseObject.addProperty("status", true);
                } else {
                    ResponseObject.addProperty("message", "Product Not Found !");
                }
            } catch (Exception e) {
                ResponseObject.addProperty("message", "Product Not Found !");
                e.printStackTrace();
            }

        }
        return gson.toJson(ResponseObject);
    }

    @Override
    public String loadProductReviews(String id) {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (RegexValidator.isValidInteger(id)) {
            try {
                Product product = em.find(Product.class, Integer.valueOf(id));

                String jpql = "SELECT r FROM Reviews r WHERE r.product = :product";
                List<Reviews> reviewsList = em.createQuery(jpql, Reviews.class)
                        .setParameter("product", product)
                        .getResultList();

                int reviewsCount = reviewsList.size();

                ResponseObject.add("reviewsList", gson.toJsonTree(reviewsList));
                ResponseObject.addProperty("reviewsCount", reviewsCount);
                ResponseObject.addProperty("status", true);
            } catch (Exception e) {
                ResponseObject.addProperty("message", "Product Not Found !");
                e.printStackTrace();
            }
        }

        return gson.toJson(ResponseObject);
    }
}
