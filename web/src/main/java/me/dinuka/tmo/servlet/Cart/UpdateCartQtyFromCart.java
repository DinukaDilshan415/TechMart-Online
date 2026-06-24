package me.dinuka.tmo.servlet.Cart;

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
import jakarta.transaction.UserTransaction;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.Product;
import me.dinuka.tmo.entity.Stock;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;
import me.dinuka.tmo.util.RegexValidator;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.List;

@WebServlet("/UpdateCartQtyFromCart")
public class UpdateCartQtyFromCart extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @EJB
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        String productId = request.getParameter("id");
        String qty = request.getParameter("qty");

        User user = (User) request.getSession().getAttribute("user");

        String s = updateCartQty(productId, qty, user);

        if (s.equals("success")) {
            ResponseObject.addProperty("message", "Cart Updated");
            ResponseObject.addProperty("status", true);
        } else {
            ResponseObject.addProperty("message", s);
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

    public String updateCartQty(String id, String qty, User user) {

        if (!RegexValidator.isValidInteger(id)) {
            return "Invalid Product Id";
        } else if (!RegexValidator.isValidInteger(qty)) {
            return "Invalid Product Qty";
        } else {
            Product product = em.find(Product.class, Integer.valueOf(id));

            if (product == null) {
                return "Product Not Found";
            }

            String stockJpql = "SELECT s FROM Stock s WHERE s.product = :product";
            List<Stock> stockResults = em.createQuery(stockJpql, Stock.class)
                    .setParameter("product", product)
                    .getResultList();
            Stock pStock = stockResults.isEmpty() ? null : stockResults.get(0);

            if (user != null) {
                String cartJpql = "SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product";
                List<Cart> cartList = em.createQuery(cartJpql, Cart.class)
                        .setParameter("user", user)
                        .setParameter("product", product)
                        .getResultList();

                if (cartList.isEmpty()) { // Product not found in user's cart
                    return "Product Not Found";
                } else {
                    Cart cart = cartList.get(0);
                    int targetQty = Integer.parseInt(qty);

                    if (pStock != null && targetQty <= pStock.getQty()) {
                        cart.setQty(targetQty);

                        System.out.println("Cart Updated:"+" Product Id: " + cart.getProduct().getId() + " Qty: " + cart.getQty());

                        UserTransaction ut = null;
                        try {
                            ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
                            ut.begin();
                            try {
                                em.merge(cart);
                                ut.commit();
                            } catch (Exception e) {
                                ut.rollback();
                                throw e;
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        return "success";
                    } else {
                        return "Insuficient Product Quantity";
                    }
                }
            } else {
                return "User Not Logged In";
            }
        }
    }
}
