package com.example.demo;

import com.example.demo.clients.EventClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.dtos.EventTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final EventClient eventClient;

    @Autowired
    public EventService(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    public List<EventDTO> getAllEvents() {
        try {
            Map<String, List<?>> response = eventClient.getEventsAndTypes().getBody();
            return (List<EventDTO>) response.get("events");
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching events from API", e);
        }
    }

    public List<EventTypeDTO> getAllEventTypes() {
        try {
            Map<String, List<?>> response = eventClient.getEventsAndTypes().getBody();
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
}
