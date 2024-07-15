package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.Services.RoleService;
import com.example.demo.clients.UserClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.EventService;
import com.example.demo.dtos.EventTypeDTO;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    private final EventService eventService;
    private RoleService roleService;
    private UserClient userClient;
    private AuthService authService;

    @Autowired
    public EventController(EventService eventService, RoleService roleService, UserClient userClient, AuthService authService) {
        this.eventService = eventService;
        this.roleService = roleService;
        this.userClient = userClient;
        this.authService = authService;
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
        } catch(FeignException.Forbidden e){
            return "redirect:/authentication/login";
        }
    }
    @GetMapping("/details/{eventName}")
    public String getEventDetails(@PathVariable String eventName, Model model) {
        return Optional.ofNullable(eventService.getEventDetails(eventName))
                .map(eventDTO -> {
                    String token = authService.getToken();
                    List<String> roles = userClient.getRoles(token).getBody();
                    if(roles != null){
                        model.addAttribute("roles", roles);
                    }
                    model.addAttribute("event", eventDTO);
                    return "event-details";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }
}

