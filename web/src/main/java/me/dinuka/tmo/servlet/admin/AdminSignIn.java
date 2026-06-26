/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package me.dinuka.tmo.servlet.admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.entity.Admin;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;
import me.dinuka.tmo.util.monitor.SessionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminSignIn")
public class AdminSignIn extends HttpServlet {

    private static final int ACTIVE = 1;

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();
        Gson gson = new Gson();
        JsonObject signIn = gson.fromJson(request.getReader(), JsonObject.class);

        final String email = signIn.get("email").getAsString();
        String password = signIn.get("password").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (email.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Email");
        } else if (password.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Your Password");
        } else {
            List<Admin> admins = em.createQuery("SELECT a FROM Admin a WHERE a.email = :email AND a.password = :password", Admin.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getResultList();

            if (admins.isEmpty()) {
                ResponseObject.addProperty("message", "Invalid Credentials !");
            } else {
                Admin admin = admins.get(0);

                if (admin.getStatus().getId() == ACTIVE) {
                    HttpSession ses = request.getSession();

                    ses.setAttribute("adminEmail", admin.getEmail());

                    Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                    sessionCookie.setPath("/");
                    sessionCookie.setHttpOnly(true);
                    sessionCookie.setSecure(false); // false for localhost HTTP
                    sessionCookie.setMaxAge(60 * 30); // 30 minutes

                    response.addCookie(sessionCookie);
                    SessionTracker.sessionCreated();

                    ResponseObject.addProperty("status", true);
                } else {
                    ResponseObject.addProperty("message", "Your Admin Account Deactivated !");
                }
            }
        }
        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
