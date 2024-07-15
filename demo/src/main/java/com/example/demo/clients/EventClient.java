package com.example.demo.clients;

import com.example.demo.dtos.EventDTO;
import com.example.demo.IndividualFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
//@FeignClient(name = "event-service", url = "http://localhost:8080/event")

@FeignClient(name = "event-service", url = "http://localhost:8080/event", configuration = IndividualFeignConfig.class)
public interface EventClient {
    @GetMapping("/all")
    ResponseEntity<Map<String, List<?>>> getEventsAndTypes();
    @GetMapping("/{eventName}")
    ResponseEntity<Map<String, EventDTO>> getEventDetails(@PathVariable String eventName);
}


