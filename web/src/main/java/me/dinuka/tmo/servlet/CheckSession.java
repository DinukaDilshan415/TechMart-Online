package me.dinuka.tmo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/CheckSession")
public class CheckSession extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");

        if (session != null && session.getAttribute("user") != null) {
            response.getWriter().write("{\"loggedIn\": true}");
        } else {
            response.getWriter().write("{\"loggedIn\": false}");
        }
    }
}
