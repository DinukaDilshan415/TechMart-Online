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
import java.util.List;

@WebServlet("/SaveTempCart")
public class SaveTempCart extends HttpServlet {

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

            if (ses.getAttribute("tempCartList") != null) {
                List<Cart> userCartList = (List<Cart>) ses.getAttribute("tempCartList");

                try {
                    em.getTransaction().begin();

                    for (Cart cart : userCartList) {
                        em.persist(cart);
                    }

                    em.getTransaction().commit();

                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    throw e;
                }
                ses.setAttribute("tempCartList", null);
                ResponseObject.addProperty("status", true);
            }

        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
