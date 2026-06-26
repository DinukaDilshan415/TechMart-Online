package me.dinuka.tmo.servlet.Cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;

@WebServlet("/RemoveFromCart")
public class RemoveFromCart extends HttpServlet {

    @EJB
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("id");

        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            DbConnectionTracker.connectionAcquired();

            String s = cartService.removeFromSessionCart(productId, user);

            if (s.equals("success")) {
                ResponseObject.addProperty("message", "Product Removed from Cart");
                ResponseObject.addProperty("status", true);
            } else {
                ResponseObject.addProperty("message", s);
            }

        } else {
            ResponseObject.addProperty("message", "Session Expired ! Please Sign in");
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
