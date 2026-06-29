package me.dinuka.tmo.dto;

import me.dinuka.tmo.entity.Category;
import me.dinuka.tmo.entity.MainCategory;

import java.util.List;

public class SearchDetailsDTO {
    private MainCategory mainCategory;
    private List<Category> categorys;
    private List<CategoriesDTO> categories;

    public SearchDetailsDTO() {
    }

    public SearchDetailsDTO(MainCategory mainCategory, List<Category> categorys, List<CategoriesDTO> categories) {
        this.mainCategory = mainCategory;
        this.categorys = categorys;
        this.categories = categories;
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

    public List<CategoriesDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesDTO> categories) {
        this.categories = categories;
    }
}
