package me.dinuka.tmo.servlet.loading;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.ProductLoadService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;

@WebServlet("/LoadProductReviews")
public class LoadProductReviews extends HttpServlet {

    @EJB
    private ProductLoadService productLoadService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("id");

        DbConnectionTracker.connectionAcquired();

        String responseText = productLoadService.loadProductReviews(productId);

        DbConnectionTracker.connectionReleased();
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
