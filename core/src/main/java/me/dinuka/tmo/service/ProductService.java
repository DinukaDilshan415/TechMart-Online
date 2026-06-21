package me.dinuka.tmo.service;

import jakarta.ejb.Remote;
import me.dinuka.tmo.dto.HomeProductsDTO;
import me.dinuka.tmo.dto.ProductDTO;

import java.util.List;

@Remote
public interface ProductService {
    String addProduct(ProductDTO productDTO);
    String updateProduct(ProductDTO productDTO);
    String deleteProduct(int id);
}
