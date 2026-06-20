package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.ProductDTO;

@Remote
public interface ProductService {
    String addProduct(ProductDTO productDTO);
    String updateProduct(ProductDTO productDTO);
    String deleteProduct(int id);
    ProductDTO getProductById(int id);
}
