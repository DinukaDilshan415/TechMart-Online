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
import me.dinuka.tmo.service.CheckoutService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.Map;

@WebServlet("/MakePayment")
public class MakePayment extends HttpServlet {
    
    @EJB(lookup = "java:global/ear/ejb/GetPaymentDetailsBean")
    private CheckoutService checkoutService;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        Gson gson = new Gson();
        JsonObject reqJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        Map<String, Object> requestData = gson.fromJson(reqJsonObject, Map.class);

        DbConnectionTracker.connectionAcquired();

        String responseText = checkoutService.getDeatilsForPayment(user, requestData);

        DbConnectionTracker.connectionReleased();
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
