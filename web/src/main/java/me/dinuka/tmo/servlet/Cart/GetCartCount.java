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

@WebServlet("/GetCartCount")
public class GetCartCount extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
//        ResponseObject.addProperty("status", false);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) { //count from session

            if (session.getAttribute("sessionCart") != null) {
                ArrayList<Cart> sessionList = (ArrayList<Cart>) session.getAttribute("sessionCart");
                int count = sessionList.size();
                ResponseObject.addProperty("count", count);
            }
        } else { //count from db
            String jpql = "SELECT COUNT(c) FROM Cart c WHERE c.user = :user";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("user", user)
                    .getSingleResult();

            ResponseObject.addProperty("count", count);

        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
