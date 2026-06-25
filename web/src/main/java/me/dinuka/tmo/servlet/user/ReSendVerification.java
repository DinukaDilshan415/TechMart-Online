package me.dinuka.tmo.servlet.user;

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
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.UserService;
import me.dinuka.tmo.util.Util;
import me.dinuka.tmo.util.mailer.Mail;
import me.dinuka.tmo.util.mailer.MailTemplates;

import java.io.IOException;

@WebServlet("/ReSendVerification")
public class ReSendVerification extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager session;

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("email") == null) {
            ResponseObject.addProperty("message", "Email not found");
        } else {
            String email = ses.getAttribute("email").toString();

            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            User user = session.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (user == null) {
                ResponseObject.addProperty("message", "User Not Found");
            } else {
                final String verificationCode = Util.generateCode();

                user.setVerification_code(verificationCode);

                String s = userService.reSendVerification(user);

                if (s.equals("success")) {
                    ses.setAttribute("user", user);

                    Mail.sendMail(email, "TechMart Account Verification", new MailTemplates().accountVerifyMail(verificationCode));

                    ResponseObject.addProperty("status", true);
                    ResponseObject.addProperty("message", "New Verification Code Send");
                } else {
                    ResponseObject.addProperty("message", "Verification Code Not Updated");
                }
            }
        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
