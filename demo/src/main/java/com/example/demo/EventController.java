package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<EventDTO> events = eventService.getAllEvents();
        List<EventTypeDTO> eventTypes = eventService.getAllEventTypes();

        model.addAttribute("events", events);
        model.addAttribute("eventTypes", eventTypes);

        return "all-events";
    }
}

