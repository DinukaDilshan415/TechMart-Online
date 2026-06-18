package me.dinuka.tmo.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.dto.TestDTO;
import me.dinuka.tmo.entity.City;
import me.dinuka.tmo.service.TestService;
import me.dinuka.tmo.util.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    @EJB
    private TestService testService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestDTO testDTO = testService.getById(1);
//        testService.save(new me.dinuka.tmo.dto.TestDTO(1,"Dinuka","dinuka@techmart.com"));

        Session session = HibernateUtil.getSessionFactory().openSession();

        City city = session.get(City.class, 1);

        System.out.println(city.getName());

        resp.getWriter().write("Hello World");
    }
}
