package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
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
}
