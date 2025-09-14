package org.itmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.itmo.service.HelloWorldService;

@Controller
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @Autowired
    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    // Этот метод будет обрабатывать GET-запросы на главный URL ("/")
    @GetMapping("/")
    public String sayHello(Model model) {
        // 1. Получаем данные из сервис-слоя
        String greeting = helloWorldService.getGreetingMessage();

        // 2. Кладем эти данные в "модель". Модель - это "контейнер",
        // который контроллер передает в шаблон (view).
        model.addAttribute("message", greeting);

        // 3. Возвращаем имя шаблона, который нужно показать пользователю.
        // Spring, благодаря WebConfig, поймет, что нужно найти "hello-page.html".
        return "hello-page";
    }
}