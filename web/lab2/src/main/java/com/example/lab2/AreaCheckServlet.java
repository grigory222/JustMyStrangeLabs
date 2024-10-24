package com.example.lab2;

import com.example.lab2.util.Calculator;
import com.example.lab2.util.ResultsBean;
import com.example.lab2.util.ValidationException;
import com.example.lab2.util.Validator;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "AreaCheckServlet", value = "/areaCheck")
public class AreaCheckServlet extends HttpServlet {
    Validator validator = new Validator();
    Calculator calculator = new Calculator();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        try {
            validator.validateParams(request.getParameter("x"), request.getParameter("y"), request.getParameter("r"));
        } catch (ValidationException e) {
            // forward куда-то...
            request.getRequestDispatcher("./error.jsp").forward(request, response);
        }

        int x = Integer.parseInt(request.getParameter("x"));
        float y = Float.parseFloat(request.getParameter("y"));
        int r = Integer.parseInt(request.getParameter("r"));
        Instant startTime = Instant.now();
        boolean isInside = calculator.calculate(x, y, r);
        Instant endTime = Instant.now();
        long executionTime = ChronoUnit.NANOS.between(startTime, endTime);
        LocalTime time = LocalTime.now();

        HttpSession session = request.getSession();
        ResultsBean results = (ResultsBean) session.getAttribute("results");
        if (results == null){
            results = new ResultsBean();
            session.setAttribute("results", results);
        }
        results.addResult(new ResultsBean.Result(String.valueOf(x), String.valueOf(y), String.valueOf(r), executionTime, time, isInside));

        request.setAttribute("x", x);
        request.setAttribute("y", y);
        request.setAttribute("r", r);
        request.setAttribute("isInside", isInside);
        request.setAttribute("executionTime", executionTime);
        request.setAttribute("time", time);

        request.getRequestDispatcher("./result.jsp").forward(request, response);
    }

}
