package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.clients.EventClient;
import com.example.demo.clients.EventTypeClient;
import com.example.demo.dtos.EventTypeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/event-type")
public class EventTypeController {
    AuthService authService;
    EventTypeClient eventTypeClient;

    @Autowired
    public EventTypeController(AuthService authService, EventTypeClient eventTypeClient) {
        this.authService = authService;
        this.eventTypeClient = eventTypeClient;
    }

    @GetMapping("/add")
    public void addEventType(Model model) {

    }

    @PostMapping("/add")
    public void postEventType(@Valid @ModelAttribute EventTypeDTO eventTypeDTO) {

    }

    @GetMapping("/all")
    public String allEventsTypes() {
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

