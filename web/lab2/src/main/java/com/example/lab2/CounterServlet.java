package com.example.lab2;

import com.example.lab2.util.CounterMap;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "CounterServlet", value = "/secret")
public class CounterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Получаем мапу заголовков из контекста
        ServletContext context = getServletContext();
        CounterMap counterMap = (CounterMap) context.getAttribute("counterMap");

        // Получаем данные и преобразуем в строку
        Map<String, Long> headerCountMap = counterMap.getHeaderCountMap();
        out.println("<html><body>");
        out.println("<h1>Количество заголовков</h1>");
        out.println("<ul>");
        for (Map.Entry<String, Long> entry : headerCountMap.entrySet()) {
            out.printf("<li>%s: %d</li>%n", entry.getKey(), entry.getValue());
        }
        out.println("</ul>");
        out.println("</body></html>");
    }

}
