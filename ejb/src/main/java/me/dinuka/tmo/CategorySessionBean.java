package me.dinuka.tmo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.dinuka.tmo.dto.ProductCategoryDTO;
import me.dinuka.tmo.service.CategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class CategorySessionBean implements CategoryService {
    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    public List<HashMap> getPopularCategories() {

        String jpql = "SELECT new me.dinuka.tmo.dto.ProductCategoryDTO(MIN(p.id), c.name) " +
                "FROM InvoiceItems ii " +
                "JOIN ii.stock st " +
                "JOIN st.product p " +
                "JOIN p.subCategory sc " +
                "JOIN sc.category c " +
                "GROUP BY c.name";

        List<ProductCategoryDTO> results = em.createQuery(jpql, ProductCategoryDTO.class)
                .getResultList();


        ArrayList<HashMap> categoryList = new ArrayList<>();

        for (ProductCategoryDTO result : results) {
            HashMap<String, Object> map = new HashMap();
            map.put("name", result.getCateName());
            map.put("image", "http://localhost:8080/techmart/product-images/"+result.getPid()+"/image1.png");
            categoryList.add(map);
        }

        return categoryList;
    }
}
