package me.dinuka.tmo.dto;

public class ProductCategoryDTO {
    private int pid;
    private String cateName;

    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(int pid, String cateName) {
        this.pid = pid;
        this.cateName = cateName;
    }

    public int getPid() { return pid; }
    public void setPid(int pid) { this.pid = pid; }

    public String getCateName() { return cateName; }
    public void setCateName(String cateName) { this.cateName = cateName; }
}
