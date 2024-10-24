package com.example.lab2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ControllerServlet", value = "/controller")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String r = request.getParameter("r");
        RequestDispatcher dispatcher;
        if (x == null || y == null || r == null){
            dispatcher = request.getRequestDispatcher("/index.jsp");
        } else {
            dispatcher = request.getRequestDispatcher("/areaCheck");
        }
        dispatcher.forward(request, response);
    }

}
