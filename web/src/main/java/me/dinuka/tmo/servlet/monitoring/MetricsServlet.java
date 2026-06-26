package me.dinuka.tmo.servlet.monitoring;

import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.service.MetricsCollectorBean;
import me.dinuka.tmo.service.MonitorService;
import me.dinuka.tmo.util.monitor.MetricsCollectorQualifier;
import me.dinuka.tmo.util.monitor.MetricsDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/metrics")
public class MetricsServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        InitialContext ctx = null;
        MetricsCollectorBean bean;
        try {
            ctx = new InitialContext();

            bean = (MetricsCollectorBean) ctx.lookup("java:global/ear/techmart/MetricsCollectorBean");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MetricsDTO metrics = bean.collect();

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(metrics));
        out.flush();
    }
}
