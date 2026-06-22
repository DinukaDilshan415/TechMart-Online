package me.dinuka.tmo.servlet.Cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;
import me.dinuka.tmo.util.RegexValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {

    @EJB
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productId = request.getParameter("id");
        String qty = request.getParameter("qty");

        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (!RegexValidator.isValidInteger(productId)) {
            ResponseObject.addProperty("message", "Invalid Product Id");
        } else if (!RegexValidator.isValidInteger(qty)) {
            ResponseObject.addProperty("message", "Invalid Product Qty");
        } else {

            User user = (User) request.getSession().getAttribute("user");

            if (user != null) { //add product to db

                String message = cartService.addProductToCart(productId, qty, user);

                if (message.equals("success")) {
                    ResponseObject.addProperty("message", "Product Added to Cart");
                    ResponseObject.addProperty("status", true);
                } else {
                    ResponseObject.addProperty("message", message);
                }

            } else { //add product to session cart
                HttpSession ses = request.getSession();

                String message = cartService.addProductToSessionCart(productId, qty);

                if (message.equals("success")) {

                    List<Cart> sessionCart = cartService.getSessionCart();

                    ses.setAttribute("sessionCart", sessionCart);

                    ResponseObject.addProperty("message", "Product Added to Session Cart");
                    ResponseObject.addProperty("status", true);

                    Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                    sessionCookie.setPath("/");
                    sessionCookie.setHttpOnly(true);
                    sessionCookie.setSecure(false); // false for localhost HTTP
                    sessionCookie.setMaxAge(60 * 30); // 30 minutes

                    response.addCookie(sessionCookie);
                } else {
                    ResponseObject.addProperty("message", message);
                }
            }

        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
