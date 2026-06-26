/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package me.dinuka.tmo.servlet.loading;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.*;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;


@WebServlet("/LoadProductData")
public class LoadProductData extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Gson gson = new Gson();

        List<Brand> brandList = em.createQuery(
                        "SELECT b FROM Brand b ORDER BY b.name ASC", Brand.class)
                .getResultList();

        List<Warehouse> warehouseList = em.createQuery(
                        "SELECT w FROM Warehouse w ORDER BY w.name ASC", Warehouse.class)
                .getResultList();

        List<MainCategory> mainCategoryList = em.createQuery(
                        "SELECT m FROM MainCategory m", MainCategory.class)
                .getResultList();

        List<Category> categoryList = em.createQuery(
                        "SELECT c FROM Category c", Category.class)
                .getResultList();

        List<SubCategory> subCategoryList = em.createQuery(
                        "SELECT s FROM SubCategory s", SubCategory.class)
                .getResultList();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", true);

        ResponseObject.add("brandList", gson.toJsonTree(brandList));
        ResponseObject.add("warehouseList", gson.toJsonTree(warehouseList));
        ResponseObject.add("mainCategoryList", gson.toJsonTree(mainCategoryList));
        ResponseObject.add("categoryList", gson.toJsonTree(categoryList));
        ResponseObject.add("subCategoryList", gson.toJsonTree(subCategoryList));


        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
