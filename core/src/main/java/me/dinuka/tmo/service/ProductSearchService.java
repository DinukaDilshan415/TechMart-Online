package me.dinuka.tmo.service;

import jakarta.ejb.Remote;

import java.util.Map;

@Remote
public interface ProductSearchService {
    String searchProducts(Map<String, Object> requestData);
    String loadSearchDetails();
}
