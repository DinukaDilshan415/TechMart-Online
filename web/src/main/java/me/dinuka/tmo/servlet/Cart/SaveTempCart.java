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
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.Cart;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.CartService;
import me.dinuka.tmo.service.TempCartService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/SaveTempCart")
public class SaveTempCart extends HttpServlet {

    @EJB
    private TempCartService tempCartService;

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

                DbConnectionTracker.connectionAcquired();

                String s = tempCartService.saveTempCart(userCartList);

                if (s.equals("success")) {
                    ses.setAttribute("tempCartList", null);
                    ResponseObject.addProperty("status", true);
                }
            }

        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
