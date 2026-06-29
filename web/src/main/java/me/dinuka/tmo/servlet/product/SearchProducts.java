package me.dinuka.tmo.servlet.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.ProductSearchService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/SearchProducts")
public class SearchProducts extends HttpServlet {

    @EJB
    private ProductSearchService productSearchService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        Map<String, Object> requestData = gson.fromJson(requestJsonObject, Map.class);

        String responseText = productSearchService.searchProducts(requestData);

        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
