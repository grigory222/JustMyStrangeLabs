package org.itmo.service;

import lombok.RequiredArgsConstructor;
import org.itmo.model.Route;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteEventsPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public void publishCreated(Route route) {
        messagingTemplate.convertAndSend("/topic/routes", new Event("created", route.getId()));
    }

    public void publishUpdated(Route route) {
        messagingTemplate.convertAndSend("/topic/routes", new Event("updated", route.getId()));
    }

    public void publishDeleted(Integer id) {
        messagingTemplate.convertAndSend("/topic/routes", new Event("deleted", id));
    }

    public record Event(String type, Integer id) {}
}


