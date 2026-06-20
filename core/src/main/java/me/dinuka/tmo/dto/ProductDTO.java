package me.dinuka.tmo.dto;

import jakarta.servlet.http.Part;

public class ProductDTO implements java.io.Serializable{
    private int id;
    private String title;
    private String description;
    private String sub_category_id;
    private String brand_id;
    private String discount;
    private String created_at;
    private String status_id;
    private String price;
    private String qty;
    private String ware_house_id;
    private byte[] image;

    public ProductDTO() {
    }

    public ProductDTO(int id, String title, String description, String sub_category_id, String brand_id, String discount, String created_at, String status_id, String price, String qty, String ware_house_id, Part image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sub_category_id = sub_category_id;
        this.brand_id = brand_id;
        this.discount = discount;
        this.created_at = created_at;
        this.status_id = status_id;
        this.price = price;
        this.qty = qty;
        this.ware_house_id = ware_house_id;
    }

    public ProductDTO(String title, String description, String sub_category_id, String brand_id, String discount, String created_at, String status_id, String price, String qty, String ware_house_id, byte[] image) {
        this.title = title;
        this.description = description;
        this.sub_category_id = sub_category_id;
        this.brand_id = brand_id;
        this.discount = discount;
        this.created_at = created_at;
        this.status_id = status_id;
        this.price = price;
        this.qty = qty;
        this.ware_house_id = ware_house_id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getWare_house_id() {
        return ware_house_id;
    }

    public void setWare_house_id(String ware_house_id) {
        this.ware_house_id = ware_house_id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
