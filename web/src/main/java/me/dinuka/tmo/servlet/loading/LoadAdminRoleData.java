package me.dinuka.tmo.servlet.loading;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.AdminRole;

import java.io.IOException;
import java.util.List;

@WebServlet("/LoadAdminRoleData")
public class LoadAdminRoleData extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        String jpql = "SELECT ar FROM AdminRole ar";
        List<AdminRole> adminRoleList = em.createQuery(jpql, AdminRole.class)
                .getResultList();

        String responseText = gson.toJson(adminRoleList);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
