/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category implements Serializable{   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    public Category() {
    }

    public Category(int id, String name, MainCategory mainCategory) {
        this.id = id;
        this.name = name;
        this.mainCategory = mainCategory;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }  
}
