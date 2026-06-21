package me.dinuka.tmo.service;

import jakarta.ejb.Remote;

import java.util.HashMap;
import java.util.List;

@Remote
public interface CategoryService {
    List<HashMap> getPopularCategories();
}
