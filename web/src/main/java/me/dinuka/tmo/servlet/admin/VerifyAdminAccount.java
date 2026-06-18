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
import jakarta.transaction.UserTransaction;
import me.dinuka.tmo.entity.Admin;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.List;


@WebServlet("/VerifyAdminAccount")
public class VerifyAdminAccount extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject verify = gson.fromJson(request.getReader(), JsonObject.class);

        final String verificationCode = verify.get("code").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (verificationCode.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Verification Code");
        } else {
            HttpSession ses = request.getSession();
            if (ses.getAttribute("adminEmail") == null) {
                ResponseObject.addProperty("message", "Email not found");
            } else {
                String email = ses.getAttribute("adminEmail").toString();

                List<Admin> admins = em.createQuery("SELECT a FROM Admin a WHERE a.email = :email AND a.verification_code = :verificationCode", Admin.class)
                        .setParameter("email", email)
                        .setParameter("verificationCode", verificationCode)
                        .getResultList();

                if (admins.isEmpty()) {
                    ResponseObject.addProperty("message", "Invalid varification code");
                } else {
                    Admin admin = admins.get(0);

                    admin.setVerification_code("Verified");

                    UserTransaction ut = null;
                    try {
                        ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
                        ut.begin();
                        try {
                            em.merge(admin);
                            ut.commit();
                        } catch (Exception e) {
                            ut.rollback();
                            throw e;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    ses.setAttribute("admin", admin);

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
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
