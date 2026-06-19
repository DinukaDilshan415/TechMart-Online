/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "gift_address")
public class GiftAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "full_name", length = 45, nullable = false)
    private String full_name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postal_code;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public GiftAddress() {
    }

    public GiftAddress(int id, String full_name, String address, String note, String postal_code, City city) {
        this.id = id;
        this.full_name = full_name;
        this.address = address;
        this.note = note;
        this.postal_code = postal_code;
        this.city = city;
    }

    public GiftAddress(String full_name, String address, String note, String postal_code, City city) {
        this.full_name = full_name;
        this.address = address;
        this.note = note;
        this.postal_code = postal_code;
        this.city = city;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}
