package me.dinuka.tmo.servlet.UserAccount;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.entity.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@WebServlet("/SetAccountData")
public class SetAccountData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (session != null && session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");

            String username = u.getUsername();
            String email = u.getEmail();
            String mobile = u.getMobile();
            Date created_at = u.getCreated_at();

            String formattedDate = new SimpleDateFormat("yyyy MMMM dd", Locale.ENGLISH).format(created_at);

            ResponseObject.addProperty("status", true);
            ResponseObject.addProperty("username", username);
            ResponseObject.addProperty("email", email);
            ResponseObject.addProperty("mobile", mobile);
            ResponseObject.addProperty("created_at", formattedDate);

//            Session s = HibernateUtil.getSessionFactory().openSession();
//            Criteria c = s.createCriteria(Address.class);
//            c.add(Restrictions.eq("user", u));
//            if (!c.list().isEmpty()) {
//                List<Address> addreassList = c.list();
//                ResponseObject.add("addreassList", gson.toJsonTree(addreassList));
//            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
