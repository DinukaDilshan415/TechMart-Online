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
import me.dinuka.tmo.util.Util;
import me.dinuka.tmo.util.mailer.Mail;
import me.dinuka.tmo.util.mailer.MailTemplates;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;

@WebServlet("/ReSendAdminVerification")
public class ReSendAdminVerification extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("adminEmail") == null) {
            ResponseObject.addProperty("message", "Email not found");
        } else {
            String email = ses.getAttribute("adminEmail").toString();

            List<Admin> admins = em.createQuery("SELECT a FROM Admin a WHERE a.email = :email", Admin.class)
                    .setParameter("email", email)
                    .getResultList();

            if (admins.isEmpty()) {
                ResponseObject.addProperty("message", "Admin Not Found");
            } else {
                Admin admin = admins.get(0);

                final String verificationCode = Util.generateCode();

                admin.setVerification_code(verificationCode);

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

                System.out.println("Verification Code: " + verificationCode);

                //send mail
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                Mail.sendMail(email, "Admin Account Verification", new MailTemplates().adminVerificationEmail(verificationCode));
//                    }
//                }).start();

                ResponseObject.addProperty("status", true);
                ResponseObject.addProperty("message", "New Verification Code Send");
            }
        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
