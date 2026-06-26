package me.dinuka.tmo.servlet.loading;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.Admin;
import me.dinuka.tmo.service.AdminDashboardService;
import me.dinuka.tmo.util.monitor.DbConnectionTracker;

import java.io.IOException;

@WebServlet("/LoadAdmins")
public class LoadAdmins extends HttpServlet {

    @EJB
    private AdminDashboardService adminDashboardService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Admin admin = (Admin) request.getSession().getAttribute("admin");

        DbConnectionTracker.connectionAcquired();

        String responseText = adminDashboardService.getUsersData(admin);

        DbConnectionTracker.connectionReleased();
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
