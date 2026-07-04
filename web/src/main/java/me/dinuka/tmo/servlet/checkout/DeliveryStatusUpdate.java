package me.dinuka.tmo.servlet.checkout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.dinuka.tmo.service.OrdersService;

import java.io.IOException;

@WebServlet("/DeliveryStatusUpdate")
public class DeliveryStatusUpdate extends HttpServlet {

    @EJB
    private OrdersService ordersService;

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject status = gson.fromJson(request.getReader(), JsonObject.class);

        String newStatus = status.get("newStatus").getAsString();
        String orderId = status.get("orderId").getAsString();

        JsonObject ResponseObject = new JsonObject();
        ResponseObject.addProperty("status", false);

        if (newStatus.isEmpty()) {
            ResponseObject.addProperty("message", "Something Wrong With Status");
        } else {
            HttpSession ses = request.getSession(false);

            if (ses != null && ses.getAttribute("admin") != null) {

                String s = ordersService.updateOrderStatus(orderId, newStatus);

                if (s.equals("success")) {
                    ResponseObject.addProperty("message", "Status Updated Successfully");
                    ResponseObject.addProperty("status", true);
                } else {
                    ResponseObject.addProperty("message", s);
                }

            } else {
                ResponseObject.addProperty("message", "Admin Session Expired");
            }
        }

        String responseText = gson.toJson(ResponseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
