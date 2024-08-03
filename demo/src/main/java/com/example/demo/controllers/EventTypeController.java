package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.clients.EventTypeClient;
import com.example.demo.dtos.EventTypeDTO;
import jakarta.validation.Valid;
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

    public EventTypeController(AuthService authService, EventTypeClient eventTypeClient) {
        this.authService = authService;
        this.eventTypeClient = eventTypeClient;
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
    public String allEventsTypes() {
        return "event-type/all-event-types";
    }

    @DeleteMapping("/delete")
    public void deleteEventType(@RequestParam("id") Integer id) {

    }

    @PutMapping("/update")
    public void updateEventType(@RequestParam("id") Integer id, @RequestBody EventTypeDTO eventTypeDTO) {


    }
}

