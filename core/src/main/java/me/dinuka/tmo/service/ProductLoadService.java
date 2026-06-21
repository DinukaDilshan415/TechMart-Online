package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.HomeProductsDTO;
import me.dinuka.tmo.dto.ProductDTO;

import java.util.List;

@Remote
public interface ProductLoadService {
    List<HomeProductsDTO> loadAllProducts();
    String getProductById(String id);
    String loadProductReviews(String id);
}
