/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package me.dinuka.tmo.servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.util.HibernateUtil;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;
import me.dinuka.tmo.util.monitor.SessionTracker;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

@WebServlet("/SignIn")
public class SignIn extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager session;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Gson gson = new Gson();
        JsonObject signIn = gson.fromJson(request.getReader(), JsonObject.class);

        final String email = signIn.get("loginEmail").getAsString();
        String password = signIn.get("loginPassword").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (email.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Email");
        } else if (password.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Your Password");
        } else {

            List<User> users = session.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getResultList();


            if (users.isEmpty()) {
                ResponseObject.addProperty("message", "Invalid Credentials !");
            } else {
                User user = (User) users.get(0);
                HttpSession ses = request.getSession();

                if (!user.getVerification_code().equals("Verified")) { //not verified                
                    ses.setAttribute("email", email);
                    ResponseObject.addProperty("message", "not verified");
                } else {
                    ses.setAttribute("user", user);
                    ResponseObject.addProperty("message", "verified");
                }

                Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                sessionCookie.setSecure(false); // false for localhost HTTP
                sessionCookie.setMaxAge(60 * 30); // 30 minutes

                response.addCookie(sessionCookie);
                SessionTracker.sessionCreated();

                ResponseObject.addProperty("status", true);
            }
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
