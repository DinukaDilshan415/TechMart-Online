package me.dinuka.tmo.dto;

import me.dinuka.tmo.entity.Category;
import me.dinuka.tmo.entity.MainCategory;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;

import java.util.List;

public class HomeProductsDTO implements java.io.Serializable{
    private MainCategory mainCategory;
    private List<Category> categorys;
    private List<Product> products;
    private List<Stock> stocks;

    public HomeProductsDTO() {
    }

    public HomeProductsDTO(MainCategory mainCategory, List<Category> categorys, List<Product> products, List<Stock> stocks) {
        this.mainCategory = mainCategory;
        this.categorys = categorys;
        this.products = products;
        this.stocks = stocks;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

    public List<Category> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<Category> categorys) {
        this.categorys = categorys;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
