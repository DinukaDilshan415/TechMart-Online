package me.dinuka.tmo.servlet.loading;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.CategoryService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet("/LoadPopulerCategories")
public class LoadPopulerCategories extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        DbConnectionTracker.connectionAcquired();

        List<HashMap> categoryList = categoryService.getPopularCategories();

        responseObject.add("categories", gson.toJsonTree(categoryList));
        responseObject.addProperty("status", true);

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
