package org.itmo.controller;

import org.itmo.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.itmo.service.HelloWorldService;

import java.util.List;

@Controller
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @Autowired
    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/")
    public String showAllRoutes(Model model) {
        // 1. Создаем тестовые данные (если их еще нет)
        helloWorldService.createTestData();

        // 2. Получаем все маршруты из БД
        List<Route> routes = helloWorldService.getAllRoutes();

        // 3. Кладем список маршрутов в модель для отображения
        model.addAttribute("routes", routes);

        // 4. Возвращаем имя шаблона
        return "hello-page";
    }
}