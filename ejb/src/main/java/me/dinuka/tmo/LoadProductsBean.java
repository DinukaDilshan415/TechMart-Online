package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.HomeProductsDTO;
import me.dinuka.tmo.dto.ProductDTO;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.service.ProductLoadService;

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
    public ProductDTO getProductById(int id) {
        return null;
    }
}
