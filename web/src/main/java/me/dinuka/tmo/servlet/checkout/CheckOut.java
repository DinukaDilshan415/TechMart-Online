package me.dinuka.tmo.servlet.checkout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CheckoutProcessingService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.Map;

@WebServlet("/CheckOut")
public class CheckOut extends HttpServlet {

    @EJB
    private CheckoutProcessingService checkoutProcessingService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject reqJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        User user = (User) request.getSession().getAttribute("user");

        Map<String, Object> requestData = gson.fromJson(reqJsonObject, Map.class);

        DbConnectionTracker.connectionAcquired();

        String responseText = checkoutProcessingService.processCheckout(user, requestData);

        DbConnectionTracker.connectionReleased();
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
