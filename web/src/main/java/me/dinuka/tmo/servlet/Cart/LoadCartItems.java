package me.dinuka.tmo.servlet.Cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/LoadCartItems")
public class LoadCartItems extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("user");

        if (user != null) {//db cart

            String jpql = "SELECT c FROM Cart c WHERE c.user = :user";
            List<Cart> cartList = em.createQuery(jpql, Cart.class)
                    .setParameter("user", user)
                    .getResultList();

            if (cartList.isEmpty()) {
                ResponseObject.addProperty("message", "Your Cart Is Empty");
            } else {

                ResponseObject.addProperty("status", true);
                ResponseObject.addProperty("message", "cart items successfully loaded");
                ResponseObject.add("cartItems", gson.toJsonTree(cartList));
            }

        } else {//session cart
            ArrayList<Cart> sessionList = (ArrayList<Cart>) request.getSession().getAttribute("sessionCart");

            if (sessionList != null) {
                if (sessionList.isEmpty()) {
                    ResponseObject.addProperty("message", "Your Cart Is Empty");
                } else {

                    ResponseObject.addProperty("status", true);
                    ResponseObject.addProperty("message", "session cart items successfully loaded");
                    ResponseObject.add("cartItems", gson.toJsonTree(sessionList));
                }
            } else {
                ResponseObject.addProperty("message", "Your Cart Is Empty");
            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
