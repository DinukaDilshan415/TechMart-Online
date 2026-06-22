package me.dinuka.tmo.servlet.Cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/CheckSessionCart")
public class CheckSessionCart extends HttpServlet {

    @EJB
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            ArrayList<Cart> sessionList = (ArrayList<Cart>) request.getSession().getAttribute("sessionCart");
            if (sessionList != null) {

                String s = cartService.addSessionCartToDatabase(sessionList, user);

                if (s.equals("success")) {
                    ResponseObject.addProperty("message", "Session Cart Items Saved to Database");
                    ResponseObject.addProperty("status", true);
                    request.getSession().setAttribute("sessionCart", null);
                } else {
                    ResponseObject.addProperty("message", s);
                }

            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
