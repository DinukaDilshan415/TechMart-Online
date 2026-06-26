package me.dinuka.tmo.servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.util.monitor.SessionTracker;

import java.io.IOException;

@WebServlet("/SignOut")
public class SignOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            session.invalidate();
            SessionTracker.sessionDestroyed();
            ResponseObject.addProperty("status", true);
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
