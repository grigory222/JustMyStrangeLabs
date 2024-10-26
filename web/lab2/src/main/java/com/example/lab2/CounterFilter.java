package com.example.lab2;

import com.example.lab2.util.CounterMap;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;

@WebFilter("/*")
public class CounterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig){
        // Создаем CounterMap и сохраняем его в контексте сервлетов
        ServletContext context = filterConfig.getServletContext();
        CounterMap counterMap = new CounterMap();
        context.setAttribute("counterMap", counterMap);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ServletContext context = request.getServletContext();
        CounterMap counterMap = (CounterMap) context.getAttribute("counterMap");

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()){
            String header = headers.nextElement();
            counterMap.incrementHeader(header);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
