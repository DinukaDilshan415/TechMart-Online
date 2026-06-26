/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package me.dinuka.tmo.servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.UserService;
import me.dinuka.tmo.util.HibernateUtil;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

@WebServlet("/VerifyAccount")
public class VerifyAccount extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager session;

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Gson gson = new Gson();
        JsonObject verify = gson.fromJson(request.getReader(), JsonObject.class);

        final String verificationCode = verify.get("verificationCode").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (verificationCode.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Verification Code");
        } else {
            HttpSession ses = request.getSession();
            if (ses.getAttribute("email") == null) {
                ResponseObject.addProperty("message", "Email not found");
            } else {
                String email = ses.getAttribute("email").toString();

                String jpql = "SELECT u FROM User u WHERE u.email = :email AND u.verification_code = :code";
                List<User> users = session.createQuery(jpql, User.class)
                        .setParameter("email", email)
                        .setParameter("code", verificationCode)
                        .getResultList();

                if (users.isEmpty()) {
                    ResponseObject.addProperty("message", "Invalid varification code");
                } else {
                    User user = (User) users.get(0);

                    user.setVerification_code("Verified");

                    String s = userService.reSendVerification(user);

                    if (s.equals("success")) {
                        ses.setAttribute("user", user);

                        Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                        sessionCookie.setPath("/");
                        sessionCookie.setHttpOnly(true);
                        sessionCookie.setSecure(false); // false for localhost HTTP
                        sessionCookie.setMaxAge(60 * 30); // 30 minutes

                        response.addCookie(sessionCookie);

                        ResponseObject.addProperty("status", true);
                        ResponseObject.addProperty("message", "Verification Success");
                    }
                }
            }
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
