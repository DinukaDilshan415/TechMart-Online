package me.dinuka.tmo.servlet.loading;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.service.OrdersService;

import java.io.IOException;

@WebServlet("/LoadUserOrders")
public class LoadUserOrders extends HttpServlet {

    @EJB
    private OrdersService ordersService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        String responseText = ordersService.loadUserOrders(user);

        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
