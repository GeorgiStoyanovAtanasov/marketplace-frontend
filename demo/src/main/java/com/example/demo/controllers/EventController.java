package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.Services.RoleService;
import com.example.demo.clients.EventClient;
import com.example.demo.clients.UserClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.EventService;
import com.example.demo.dtos.EventTypeDTO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    private final EventService eventService;
    private RoleService roleService;
    private UserClient userClient;
    private AuthService authService;
    private EventClient eventClient;

    @Autowired
    public EventController(EventService eventService, RoleService roleService, UserClient userClient, AuthService authService, EventClient eventClient) {
        this.eventService = eventService;
        this.roleService = roleService;
        this.userClient = userClient;
        this.authService = authService;
        this.eventClient = eventClient;
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        try {
            List<EventDTO> events = eventService.getAllEvents();
            List<EventTypeDTO> eventTypes = eventService.getAllEventTypes();


//            String token = authService.getToken();
//            List<String> roles = userClient.getRoles(token).getBody();
//
//
//            model.addAttribute("roles", roles);
            model.addAttribute("allEvents", events);
            model.addAttribute("allTypes", eventTypes);


            return "all-events";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }

    @GetMapping("/search")
    public String getFilteredEvents(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "place", required = false) String place,
                                    @RequestParam(name = "type", required = false) Integer type,
                                    @RequestParam(name = "date", required = false) String date,
                                    @RequestParam(name = "minPrice", required = false) Double minPrice,
                                    @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                    Model model) {
        try {
            List<EventDTO> events = eventService.getFilteredEvents(name, place, type, date, minPrice, maxPrice);
            List<EventTypeDTO> eventTypes = eventService.getFilteredEventTypes(name, place, type, date, minPrice, maxPrice);

            model.addAttribute("allEvents", events);
            model.addAttribute("allTypes", eventTypes);
            return "all-events";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }

    @GetMapping("/details/{eventName}")
    public String getEventDetails(@PathVariable String eventName, Model model) {
        return Optional.ofNullable(eventService.getEventDetails(eventName))
                .map(eventDTO -> {
                    String token = authService.getToken();
                    List<String> roles = userClient.getRoles(token).getBody();
                    if (roles != null) {
                        model.addAttribute("roles", roles);
                    }
                    model.addAttribute("event", eventDTO);
                    return "event-details";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @PostMapping("/delete/{eventName}")
    public String deleteEventByName(@PathVariable String eventName, Model model) {
        try {
            eventClient.deleteEvent(eventName);
            return "redirect:/events";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/apply/{id}")
    public String applyToEvent(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            eventClient.apply(id);
         return "redirect:/events";
        } catch (RuntimeException e) {
            // Add any attributes to be used after redirection if needed
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to apply for the event.");
            return "redirect:/events";
        }
    }
}


