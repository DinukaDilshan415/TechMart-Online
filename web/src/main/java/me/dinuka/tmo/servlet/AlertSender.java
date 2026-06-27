package me.dinuka.tmo.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.jms.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.dinuka.tmo.util.monitor.JmsMetrics;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(urlPatterns = { "/AlertSender" , "/SendPromotionMessage"})
public class AlertSender extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String input = req.getParameter("input");

        try {
            InitialContext context = new InitialContext();
            TopicConnectionFactory factory =
                    (TopicConnectionFactory) context.lookup("jms/myAlertConnectionFactory");

            TopicConnection connection = factory.createTopicConnection();
            connection.start();

            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = (Topic) context.lookup("jms/myAlerts");

            TopicPublisher publisher = session.createPublisher(topic);

            publisher.publish(session.createTextMessage(input));

//            while (true) {}

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject data = gson.fromJson(req.getReader(), JsonObject.class);

        String message = data.get("message").getAsString();

        try {
            InitialContext context = new InitialContext();
            TopicConnectionFactory factory =
                    (TopicConnectionFactory) context.lookup("jms/myAlertConnectionFactory");

            TopicConnection connection = factory.createTopicConnection();
            connection.start();

            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = (Topic) context.lookup("jms/myAlerts");

            TopicPublisher publisher = session.createPublisher(topic);

            JmsMetrics.messageSent();

            publisher.publish(session.createTextMessage(message));


//            while (true) {}

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
