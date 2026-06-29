package me.dinuka.tmo.servlet.loading;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.ProductSearchService;

import java.io.IOException;

@WebServlet("/loadSearchDetails")
public class loadSearchDetails extends HttpServlet {

    @EJB
    private ProductSearchService productSearchService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String responseText = productSearchService.loadSearchDetails();

        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
