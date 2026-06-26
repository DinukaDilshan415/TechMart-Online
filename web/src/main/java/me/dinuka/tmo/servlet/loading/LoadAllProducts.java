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
import me.dinuka.tmo.entity.Admin;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/LoadAllProducts")
public class LoadAllProducts extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Admin admin = (Admin) request.getSession().getAttribute("admin");

        if (admin != null) {
            String productJpql = "SELECT p FROM Product p";
            List<Product> productList = em.createQuery(productJpql, Product.class)
                    .getResultList();

            if (!productList.isEmpty()) {
                String stockJpql = "SELECT s FROM Stock s WHERE s.product IN :products";
                List<Stock> stockList = em.createQuery(stockJpql, Stock.class)
                        .setParameter("products", productList)
                        .getResultList();

                responseObject.add("productList", gson.toJsonTree(productList));
                responseObject.add("stockList", gson.toJsonTree(stockList));
                responseObject.addProperty("status", true);
            }

        } else {
            responseObject.addProperty("message", "Admin Session Expired");
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
