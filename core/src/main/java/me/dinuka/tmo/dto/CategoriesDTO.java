package me.dinuka.tmo.dto;

import me.dinuka.tmo.entity.Category;
import me.dinuka.tmo.entity.SubCategory;

import java.util.List;

public class CategoriesDTO {
    private Category category;
    private List<SubCategory> subCategories;

    public CategoriesDTO() {
    }

    public CategoriesDTO(Category category, List<SubCategory> subCategories) {
        this.category = category;
        this.subCategories = subCategories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
