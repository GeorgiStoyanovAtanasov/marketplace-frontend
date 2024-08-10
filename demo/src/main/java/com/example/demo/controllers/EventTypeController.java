package com.example.demo.controllers;

import com.example.demo.EventService;
import com.example.demo.Services.AuthService;
import com.example.demo.clients.EventTypeClient;
import com.example.demo.dtos.EventTypeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/event-type")
public class EventTypeController {
   private AuthService authService;
   private EventTypeClient eventTypeClient;
   private EventService eventService;

    @Autowired
    public EventTypeController(AuthService authService, EventTypeClient eventTypeClient, EventService eventService) {
        this.authService = authService;
        this.eventTypeClient = eventTypeClient;
        this.eventService = eventService;
    }

    @GetMapping("/add")
    public String addEventType(Model model) {
        authService.getRoles(model);
        model.addAttribute("eventTypeDTO", new EventTypeDTO());
        return "event-type/event-type-form";
    }

    @PostMapping("/submit")
    public String postEventType(@Valid @ModelAttribute EventTypeDTO eventTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "event-type/event-type-form";
        }else {
            eventTypeClient.postEventType(eventTypeDTO);
            return "redirect:/events";
        }
    }

    @GetMapping("/all")
    public String allEventsTypes(Model model) {
        List<EventTypeDTO> allEventTypes = eventService.getAllEventTypes();
        model.addAttribute("eventTypes", allEventTypes);
        authService.getRoles(model);
        return "event-type/all-event-types";
    }

    @DeleteMapping("/delete")
    public void deleteEventType(@RequestParam("id") Integer id) {

    }

    @GetMapping("/update/{id}")
    public String updateEventType(@PathVariable Integer id, Model model) {
        model.addAttribute("updateEventType", new EventTypeDTO());
        model.addAttribute("eventTypeId", id);
        authService.getRoles(model);
        return "event-type/event-type-update-form";
    }

    @PostMapping("/update")
    public String updateEventType(@RequestParam("id") Integer id, @ModelAttribute EventTypeDTO eventTypeDTO, Model model) {
        eventTypeClient.updateEventType(id, eventTypeDTO);
        return "redirect:/event-type/all";
    }
}

