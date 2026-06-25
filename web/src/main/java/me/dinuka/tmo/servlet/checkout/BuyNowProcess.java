package me.dinuka.tmo.servlet.checkout;

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
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CheckoutService;
import me.dinuka.tmo.util.RegexValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/BuyNowProcess")
public class BuyNowProcess extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @EJB
    private CheckoutService checkoutService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        String productId = request.getParameter("id");
        String qty = request.getParameter("qty");

        HttpSession ses = request.getSession();
        User user = (User) ses.getAttribute("user");

        if (user == null) {
            ResponseObject.addProperty("message", "Please SignIn For Purchase !");
        } else {
            if (!RegexValidator.isValidInteger(productId)) {
                ResponseObject.addProperty("message", "Invalid Product Id");
            } else if (!RegexValidator.isValidInteger(qty)) {
                ResponseObject.addProperty("message", "Invalid Product Qty");
            } else {

                String jpql = "SELECT c FROM Cart c WHERE c.user = :user";
                List<Cart> userCartList = em.createQuery(jpql, Cart.class)
                        .setParameter("user", user)
                        .getResultList();

                ses.setAttribute("tempCartList", userCartList);

                String s = checkoutService.buyProductNow(userCartList, productId, qty,user);

                if (s.equals("success")) {
                    ResponseObject.addProperty("message", "Product Added to Cart");
                    ResponseObject.addProperty("status", true);
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
