package com.example.demo;

import com.example.demo.EventPermission.EventPermission;
import com.example.demo.Services.AuthService;
import com.example.demo.clients.EventClient;
import com.example.demo.clients.OrganisationClient;
import com.example.demo.clients.UserClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.dtos.EventTypeDTO;
import com.example.demo.models.Organisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EventService {

    private final EventClient eventClient;
    private final UserClient userClient;
    private AuthService authService;
    private OrganisationClient organisationClient;

    @Autowired
    public EventService(EventClient eventClient, UserClient userClient, AuthService authService, OrganisationClient organisationClient) {
        this.eventClient = eventClient;
        this.userClient = userClient;
        this.authService = authService;
        this.organisationClient = organisationClient;
    }

    public List<EventDTO> getAllEventsForShowing() {
        try {
            Map<String, List<?>> response = eventClient.getEventsAndTypes(EventPermission.ACCEPT).getBody();
            return (List<EventDTO>) response.get("events");
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching events from API", e);
        }
    }

    public List<EventDTO> getFilteredEvents(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "place", required = false) String place,
                                            @RequestParam(name = "type", required = false) Integer type,
                                            @RequestParam(name = "date", required = false) String date,
                                            @RequestParam(name = "minPrice", required = false) Double minPrice,
                                            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                            @RequestParam(name = "eventPermission", required = false) EventPermission eventPermission) {
        try {
            Map<String, List<?>> response = eventClient.searchEvents(name, place, type, date, minPrice, maxPrice, eventPermission).getBody();
            return (List<EventDTO>) response.get("events");
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching events from API", e);
        }
    }

    public List<EventTypeDTO> getAllEventTypes() {
        try {
            // Call the API client to fetch event data
            Map<String, List<?>> response = eventClient.getEventsAndTypes(EventPermission.ACCEPT).getBody();

            // Extract the list of event types from the response
            List<?> eventTypeMaps = response.get("eventTypes");

            // Initialize the ObjectMapper for conversion
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the list of LinkedHashMaps to a list of EventTypeDTOs
            List<EventTypeDTO> eventTypeDTOS = objectMapper.convertValue(
                    eventTypeMaps,
                    new TypeReference<List<EventTypeDTO>>() {}
            );

            return eventTypeDTOS;
        } catch (RestClientException e) {
            // Handle the error when fetching event types from the API
            throw new RuntimeException("Error fetching event types from API", e);
        }
    }
    public List<EventTypeDTO> getFilteredEventTypes(@RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "place", required = false) String place,
                                                    @RequestParam(name = "type", required = false) Integer type,
                                                    @RequestParam(name = "date", required = false) String date,
                                                    @RequestParam(name = "minPrice", required = false) Double minPrice,
                                                    @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                                    @RequestParam(name = "eventPermission", required = false) EventPermission eventPermission) {
        try {
            Map<String, List<?>> response = eventClient.searchEvents(name, place, type, date, minPrice, maxPrice, eventPermission).getBody();
            return (List<EventTypeDTO>) response.get("eventTypes");
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching event types from API", e);
        }
    }
    public EventDTO getEventDetails(String eventName) {
        ResponseEntity<Map<String, EventDTO>> responseEntity = eventClient.getEventDetails(eventName);
        Map<String, EventDTO> response = responseEntity.getBody();

        if (response != null && response.containsKey("event")) {
            return response.get("event");
        } else {
            return null;
        }
    }
    public boolean isUserOnEvent(EventDTO eventDTO){
        boolean isUserOnEvent = false;
        String userEmail = userClient.getEmail().getBody();
        for (int i = 0; i < eventDTO.getUsers().size(); i++) {
            if (eventDTO.getUsers().get(i).getEmail().equals(userEmail)) {
                isUserOnEvent = true;
                break;
            }
        }
        return isUserOnEvent;
    }
    public String submitEvent(@Valid @ModelAttribute EventDTO eventDTO, BindingResult bindingResult,
                              @RequestParam("organisationId") Integer organisationId,
                              @RequestParam("eventTypeId") Integer eventTypeId,
                              @RequestParam("file") MultipartFile file,
                              Model model) throws IOException {
        List<Organisation> organisations = (List<Organisation>) organisationClient.allOrganisations().getBody();
        for (int i = 0; i < organisations.size(); i++) {
            if (Objects.equals(organisationId, organisations.get(i).getId())) {
                Organisation organisation = organisations.get(i);
                eventDTO.setOrganisation(organisation);
            }
        }
        List<EventTypeDTO> eventTypeDTOS = getAllEventTypes();
        for (int i = 0; i < eventTypeDTOS.size(); i++) {
            if(Objects.equals(eventTypeId, eventTypeDTOS.get(i).getId())){
                EventTypeDTO eventTypeDTO= eventTypeDTOS.get(i);
                eventDTO.setEventTypeDTO(eventTypeDTO);
            }
        }
//        if(eventDTO.getOrganisation() == null){
//            return "token";
//        }
//        if(eventDTO.getEventTypeDTO() == null){
//            return "token";
//        }
        if(file == null){
            return "token";
        }
        byte[] fileBytes = file.getBytes();
        String encodedImage = Base64.getEncoder().encodeToString(fileBytes);
        eventDTO.setImage(encodedImage);
        if (bindingResult.hasErrors()) {
            authService.getRoles(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "event/event-form";
        } else {
            eventClient.postEvent(eventDTO);
            return "redirect:/events";
        }
    }
}
