package me.dinuka.tmo.servlet.UserAccount;

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
import me.dinuka.tmo.entity.UserAddress;
import me.dinuka.tmo.service.AddressService;
import me.dinuka.tmo.util.RegexValidator;
import me.dinuka.tmo.util.Util;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;
import java.util.List;

@WebServlet("/SaveNewAddress")
public class SaveNewAddress extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @EJB
    private AddressService addressService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbConnectionTracker.connectionAcquired();

        Gson gson = new Gson();
        JsonObject addressData = gson.fromJson(request.getReader(), JsonObject.class);

        String type = addressData.get("type").getAsString();
        String fullName = addressData.get("full_name").getAsString();
        String address = addressData.get("address").getAsString();
        String cityId = addressData.get("cityName").getAsString();
        String pCode = addressData.get("postal_code").getAsString();
        String mobile = addressData.get("mobile").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (type.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Some Type Like (Home / Work)");
        } else if (fullName.isEmpty()) {
            ResponseObject.addProperty("message", "Please Enter Full Name For Address");
        } else if (address.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Full Address");
        } else if (cityId.equals("0")) {
            ResponseObject.addProperty("message", "Please select city");
        } else if (pCode.isEmpty()) {
            ResponseObject.addProperty("message", "Postalcode can not be empty");
        } else if (!Util.isCodeValid(pCode)) {
            ResponseObject.addProperty("message", "Enter Valid Postal Code");
        } else if (mobile.isEmpty()) {
            ResponseObject.addProperty("message", "Enter Mobile Number");
        } else if (!RegexValidator.isValidSlPhone(mobile)) {
            ResponseObject.addProperty("message", "Enter Valid Mobile Number");
        }else {

            HttpSession ses = request.getSession();
            if (ses.getAttribute("user") != null) {
                User user = (User) ses.getAttribute("user");

                String message = addressService.saveNewAddress(type, fullName, address, pCode, mobile, cityId, user);

                if (message.equals("success")) {
                    ResponseObject.addProperty("message", "Address Saved Successfully");
                    ResponseObject.addProperty("status", true);
                }

            }
        }

        DbConnectionTracker.connectionReleased();
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        Gson gson = new Gson();
        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (session != null && session.getAttribute("user") != null) {
            User u = (User) session.getAttribute("user");

            String jpql = "SELECT ua FROM UserAddress ua WHERE ua.user = :user";
            List<UserAddress> addressList = em.createQuery(jpql, UserAddress.class)
                    .setParameter("user", u)
                    .getResultList();

            if (!addressList.isEmpty()) {
                ResponseObject.add("addreassList", gson.toJsonTree(addressList));
            }
        }
        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
