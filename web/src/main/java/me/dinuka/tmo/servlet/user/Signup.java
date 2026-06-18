package me.dinuka.tmo.servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import me.dinuka.tmo.dto.UserDTO;
import me.dinuka.tmo.service.UserService;
import me.dinuka.tmo.util.RegexValidator;

import java.io.IOException;

@WebServlet("/signup")
public class Signup extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(req.getReader(), JsonObject.class);

        String username = user.get("username").getAsString();
        final String email = user.get("email").getAsString();
        final String mobile = user.get("mobile").getAsString();
        String password = user.get("password").getAsString();
        String comfpassword = user.get("comfpassword").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (username.isEmpty()) {
            ResponseObject.addProperty("message", "The Username is required");
        } else if (email.isEmpty()) {
            ResponseObject.addProperty("message", "The Email is required");
        } else if (!RegexValidator.isValidEmail(email)) {
            ResponseObject.addProperty("message", "Please Enter Valid Email");
        } else if (mobile.isEmpty()) {
            ResponseObject.addProperty("message", "The Mobile Number is required");
        } else if (!RegexValidator.isValidSlPhone(mobile)) {
            ResponseObject.addProperty("message", "Please Enter Valid Mobile Number");
        } else if (password.isEmpty()) {
            ResponseObject.addProperty("message", "The Passowrd is required");
        } else if (!RegexValidator.isValidPassword(password)) {
            ResponseObject.addProperty("message", "The Password must contain at least "
                    + "uppercase, lowercase, numbers, special character and to be 8 characters long !");
        } else if (comfpassword.isEmpty()) {
            ResponseObject.addProperty("message", "The Re-Enter Passowrd ");
        } else if (!comfpassword.equals(password)) {
            ResponseObject.addProperty("message", "Please Enter Same Password");
        } else {
            String save = userService.save(new UserDTO(username, email, mobile, password, comfpassword));

            if (save.equals("success")) {
                //session creation
                HttpSession ses = req.getSession();
                ses.setAttribute("email", email);

                Cookie sessionCookie = new Cookie("JSESSIONID", ses.getId());
                sessionCookie.setPath("/");
                sessionCookie.setHttpOnly(true);
                sessionCookie.setSecure(false); // false for localhost HTTP
                sessionCookie.setMaxAge(60 * 30); // 30 minutes

                resp.addCookie(sessionCookie);

                ResponseObject.addProperty("status", true);
                ResponseObject.addProperty("message", "Registration Success ! Please Check Your Email For The Verification");
            } else if (save.equals("email-exists")) {
                ResponseObject.addProperty("message", "User with this email already exists !");
            } else {
                ResponseObject.addProperty("message", "Registration Failed !");
            }
        }
        String responseText = gson.toJson(ResponseObject);
        resp.setContentType("application/json");
        resp.getWriter().write(responseText);
    }
}
