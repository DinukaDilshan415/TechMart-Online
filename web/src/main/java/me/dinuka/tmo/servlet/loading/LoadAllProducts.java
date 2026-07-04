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
import me.dinuka.tmo.entity.Admin;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.service.AdminDashboardService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/LoadAllProducts")
public class LoadAllProducts extends HttpServlet {

    @EJB
    private AdminDashboardService adminDashboardService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Admin admin = (Admin) request.getSession().getAttribute("admin");

        String responseText = adminDashboardService.getProductsData(admin);

        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
