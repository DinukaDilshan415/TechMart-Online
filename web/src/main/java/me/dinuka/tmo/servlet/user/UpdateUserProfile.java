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
import me.dinuka.tmo.util.RegexValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/UpdateUserProfile")
public class UpdateUserProfile extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @EJB
    private UserService userService;

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String username = user.get("username").getAsString();
        final String mobile = user.get("mobile").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (username.isEmpty()) {
            ResponseObject.addProperty("message", "The Username is required");
        } else if (mobile.isEmpty()) {
            ResponseObject.addProperty("message", "The Mobile Number is required");
        } else if (!RegexValidator.isValidSlPhone(mobile)) {
            ResponseObject.addProperty("message", "Please Enter Valid Mobile Number");
        } else {
            HttpSession ses = request.getSession(false);

            if (ses != null && ses.getAttribute("user") != null) {
                User u = (User) ses.getAttribute("user");

                String jpql = "SELECT u FROM User u WHERE u.email = :email";
                List<User> userList = em.createQuery(jpql, User.class)
                        .setParameter("email", u.getEmail())
                        .getResultList();

                if (!userList.isEmpty()) {
                    User u1 = userList.get(0);

                    u1.setUsername(username);
                    u1.setMobile(mobile);

                    ses.setAttribute("user", u1);

                    Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                    sessionCookie.setPath("/");
                    sessionCookie.setHttpOnly(true);
                    sessionCookie.setSecure(false); // false for localhost HTTP
                    sessionCookie.setMaxAge(60 * 30); // 30 minutes
                    response.addCookie(sessionCookie);

                    String s = userService.update(u1);

                    if (s.equals("success")) {
                        ResponseObject.addProperty("status", true);
                        ResponseObject.addProperty("message", "Profile Updated");
                    }
                }
            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
