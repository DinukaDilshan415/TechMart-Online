package me.dinuka.tmo.servlet;

import jakarta.jms.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/AlertReceiver")
public class AlertReceiver extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long start = System.nanoTime();

        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        PrintWriter writer = resp.getWriter();

        TopicConnection connection = null;
        TopicSession session = null;
        TopicSubscriber subscriber = null;

        try {
            InitialContext context = new InitialContext();
            TopicConnectionFactory factory =
                    (TopicConnectionFactory) context.lookup("jms/myAlertConnectionFactory");

            connection = factory.createTopicConnection();
            connection.start();

            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = (Topic) context.lookup("jms/myAlerts");

            subscriber = session.createSubscriber(topic);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message message = subscriber.receive(1000);

                    if (message != null && message instanceof TextMessage) {
                        String messageBody = ((TextMessage) message).getText();

                        writer.println("data: " + messageBody);
                        writer.println();
                        writer.flush();

                        long end = System.nanoTime();

                        System.out.println("INFO: JMS Message Received: in " + ((end - start) / 1_000_000.0) + "ms");
                    }
                } catch (JMSException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Error in AlertReceiver: " + e.getMessage());
            e.printStackTrace();
            writer.println("data: Error - " + e.getMessage());
            writer.println();
            writer.flush();
        } finally {
            try {
                if (subscriber != null) subscriber.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                System.err.println("Error closing JMS resources: " + e.getMessage());
            }
            if (writer != null) writer.close();
        }
    }
}

