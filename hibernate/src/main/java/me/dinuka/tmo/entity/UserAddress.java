/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "user_address")
public class UserAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @Column(name = "full_name", length = 45, nullable = false)
    private String full_name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postal_code;
    
    @Column(name = "mobile", length = 45, nullable = false)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserAddress() {
    }

    public UserAddress(int id, String type, String full_name, String address, String postal_code, City city, User user) {
        this.id = id;
        this.type = type;
        this.full_name = full_name;
        this.address = address;
        this.postal_code = postal_code;
        this.city = city;
        this.user = user;
    }

    public UserAddress(String type, String full_name, String address, String postal_code, String mobile, City city, User user) {
        this.type = type;
        this.full_name = full_name;
        this.address = address;
        this.postal_code = postal_code;
        this.mobile = mobile;
        this.city = city;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
