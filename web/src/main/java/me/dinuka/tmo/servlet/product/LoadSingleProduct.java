package me.dinuka.tmo.servlet.product;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.ProductLoadService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;

@WebServlet("/LoadSingleProduct")
public class LoadSingleProduct extends HttpServlet {

    @EJB
    private ProductLoadService productLoadService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("id");

        DbConnectionTracker.connectionAcquired();

        String responseText = productLoadService.getProductById(productId);

        DbConnectionTracker.connectionReleased();
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
