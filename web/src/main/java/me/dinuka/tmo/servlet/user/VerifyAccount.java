/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package me.dinuka.tmo.servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.util.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

@WebServlet("/VerifyAccount")
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

                Session session = HibernateUtil.getSessionFactory().openSession();

                List<User> users = session.createQuery(
                                "from User where email = :email and verification_code = :code", User.class)
                        .setParameter("email", email)
                        .setParameter("code", verificationCode)
                        .getResultList();

                if (users.isEmpty()) {
                    ResponseObject.addProperty("message", "Invalid varification code");
                } else {
                    User user = (User) users.get(0);

                    user.setVerification_code("Verified");
                    session.merge(user);
                    session.beginTransaction().commit();

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
                session.close();
            }
        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
