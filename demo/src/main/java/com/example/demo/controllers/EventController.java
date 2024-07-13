package com.example.demo.controllers;

import com.example.demo.dtos.EventDTO;
import com.example.demo.EventService;
import com.example.demo.dtos.EventTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

        model.addAttribute("allEvents", events);
        model.addAttribute("allTypes", eventTypes);

        return "all-events";
    }
    @GetMapping("/details/{eventName}")
    public String getEventDetails(@PathVariable String eventName, Model model) {
        return Optional.ofNullable(eventService.getEventDetails(eventName))
                .map(eventDTO -> {
                    model.addAttribute("event", eventDTO);
                    return "event-details";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }
}

