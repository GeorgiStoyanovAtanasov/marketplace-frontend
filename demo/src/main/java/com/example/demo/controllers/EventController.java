package com.example.demo.controllers;

import com.example.demo.EventPermission.EventPermission;
import com.example.demo.Services.AuthService;
import com.example.demo.Services.RoleService;
import com.example.demo.clients.EventClient;
import com.example.demo.clients.OrganisationClient;
import com.example.demo.clients.UserClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.EventService;
import com.example.demo.dtos.EventTypeDTO;
import com.example.demo.models.Organisation;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class EventController {

    private final EventService eventService;
    private RoleService roleService;
    private UserClient userClient;
    private AuthService authService;
    private EventClient eventClient;
    private OrganisationClient organisationClient;

    @Autowired
    public EventController(EventService eventService, RoleService roleService, UserClient userClient, AuthService authService, EventClient eventClient, OrganisationClient organisationClient) {
        this.eventService = eventService;
        this.roleService = roleService;
        this.userClient = userClient;
        this.authService = authService;
        this.eventClient = eventClient;
        this.organisationClient = organisationClient;
    }

    @GetMapping("/home")
    public String home(Model model) {
        authService.getRoles(model);
        return "home";
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        List<EventDTO> events = eventService.getAllEventsForShowing();
        List<EventTypeDTO> eventTypes = eventService.getAllEventTypes();


        authService.getRoles(model);

        model.addAttribute("allEvents", events);
        model.addAttribute("allTypes", eventTypes);


        return "event/all-events";
    }

    @GetMapping("/events-waiting")
    public String getAllWaitingEvents(Model model) {
        Map<String, List<?>> response = eventClient.getEventsAndTypes(EventPermission.WAITING).getBody();
        List<EventTypeDTO> eventTypes = eventService.getAllEventTypes();


        authService.getRoles(model);

        model.addAttribute("allEvents", response.get("events"));
        model.addAttribute("allTypes", eventTypes);

        return "event/waiting-events";
    }

    @PostMapping("/event/accept")
    public String accept(@RequestParam(name = "id") Integer id) {
        eventClient.acceptEvent(id);
        return "redirect:/events-waiting";
    }

    @PostMapping("/event/reject")
    public String reject(@RequestParam(name = "id") Integer id) {
        eventClient.rejectEvent(id);
        return "redirect:/events-waiting";
    }

    @GetMapping("/add")
    public String addEvent(Model model) {
        authService.getRoles(model);
        List<EventTypeDTO> eventTypes = eventService.getAllEventTypes();
        Iterable<Organisation> organisations = organisationClient.allOrganisations().getBody();
        model.addAttribute("eventDTO", new EventDTO());
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("organisations", organisations);
        return "event/event-form";
    }

    @PostMapping("/submit")
    public String postEvent(@Valid @ModelAttribute EventDTO eventDTO, BindingResult bindingResult,
                            @RequestParam("organisationId") Integer organisationId,
                            @RequestParam("eventTypeId") Integer eventTypeId,
                            @RequestParam("file") MultipartFile file,
                            Model model) throws ParseException, IOException {

        // eventDTO.setFile(file);
        return eventService.submitEvent(eventDTO, bindingResult,
                organisationId,
                eventTypeId,
                file,
                model);

    }

    @GetMapping("/search")
    public String getFilteredEvents(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "place", required = false) String place,
                                    @RequestParam(name = "type", required = false) Integer type,
                                    @RequestParam(name = "date", required = false) String date,
                                    @RequestParam(name = "minPrice", required = false) Double minPrice,
                                    @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                    @RequestParam(name = "eventPermission", required = false) EventPermission eventPermission,
                                    Model model) {
        try {
            List<EventDTO> events = eventService.getFilteredEvents(name, place, type, date, minPrice, maxPrice, eventPermission);
            List<EventTypeDTO> eventTypes = eventService.getFilteredEventTypes(name, place, type, date, minPrice, maxPrice, eventPermission);
            authService.getRoles(model);
            model.addAttribute("allEvents", events);
            model.addAttribute("allTypes", eventTypes);
            return "event/all-events";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }
    @GetMapping("/search/admin")
    public String getFilteredEventsForAdmin(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "place", required = false) String place,
                                    @RequestParam(name = "type", required = false) Integer type,
                                    @RequestParam(name = "date", required = false) String date,
                                    @RequestParam(name = "minPrice", required = false) Double minPrice,
                                    @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                    @RequestParam(name = "eventPermission", required = false) EventPermission eventPermission,
                                    Model model) {
        try {
            List<EventDTO> events = eventService.getFilteredEvents(name, place, type, date, minPrice, maxPrice, eventPermission);
            List<EventTypeDTO> eventTypes = eventService.getFilteredEventTypes(name, place, type, date, minPrice, maxPrice, eventPermission);
            authService.getRoles(model);
            model.addAttribute("allEvents", events);
            model.addAttribute("allTypes", eventTypes);
            return "event/waiting-events";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }
    @GetMapping("/details/{eventName}")
    public String getEventDetails(@PathVariable String eventName, Model model) {
        return Optional.ofNullable(eventService.getEventDetails(eventName))
                .map(eventDTO -> {
                    authService.getRoles(model);
                    List<String> roles = (List<String>) model.getAttribute("roles");
                    if (!roles.isEmpty()) {
                        model.addAttribute("alreadyApplied", eventService.isUserOnEvent(eventDTO));
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
        } catch (FeignException.Forbidden e){
            return "redirect:/authentication/login";
        } catch (RuntimeException e) {
            // Add any attributes to be used after redirection if needed
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to apply for the event.");
            return "redirect:/events";
        }
    }

    @GetMapping("/event-types")
    public String getAllEventTypes(Model model) {
        if (!authService.hasSession()) {
            return "redirect:/authentication/login";
        }
        authService.getRoles(model);
        model.addAttribute("eventTypes", eventService.getAllEventTypes());
        return "event-type/all-event-types";
    }
}


