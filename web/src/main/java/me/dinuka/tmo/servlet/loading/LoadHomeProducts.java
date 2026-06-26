package me.dinuka.tmo.servlet.loading;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.dto.HomeProductsDTO;
import me.dinuka.tmo.service.ProductLoadService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/LoadHomeProducts")
public class LoadHomeProducts extends HttpServlet {

    @EJB
    private ProductLoadService productLoadService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        DbConnectionTracker.connectionAcquired();

        List<HomeProductsDTO> homeProductsList = productLoadService.loadAllProducts();

//        System.out.println(homeProductsList.size());
        responseObject.add("product", gson.toJsonTree(homeProductsList));
        responseObject.addProperty("status", true);


        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
