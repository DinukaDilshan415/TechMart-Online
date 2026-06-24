package me.dinuka.tmo.servlet.loading;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.entity.City;

import java.io.IOException;
import java.util.List;

@WebServlet("/LoadCityData")
public class LoadCityData extends HttpServlet {

    @PersistenceContext(unitName = "TechMartPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        String jpql = "SELECT c FROM City c ORDER BY c.name ASC";
        List<City> cityList = em.createQuery(jpql, City.class)
                .getResultList();

        String responseText = gson.toJson(cityList);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }
}
