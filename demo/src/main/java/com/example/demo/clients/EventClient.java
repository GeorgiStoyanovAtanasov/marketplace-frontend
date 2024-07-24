package com.example.demo.clients;

import com.example.demo.dtos.EventDTO;
import com.example.demo.IndividualFeignConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//@FeignClient(name = "event-service", url = "http://localhost:8080/event")

@FeignClient(name = "event-service", url = "${event-service.url}", configuration = IndividualFeignConfig.class)
public interface EventClient {
    @GetMapping("/all")
    ResponseEntity<Map<String, List<?>>> getEventsAndTypes();

    @GetMapping("/{eventName}")
    ResponseEntity<Map<String, EventDTO>> getEventDetails(@PathVariable String eventName);
    @GetMapping("/search")
    ResponseEntity<Map<String, List<?>>> searchEvents(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "place", required = false) String place,
            @RequestParam(name = "type", required = false) Integer type,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice
    );
    @DeleteMapping("/delete")
    void deleteEvent(@RequestParam("name") String name);
    @PostMapping("/apply")
    void apply(@RequestParam(name = "id") Integer id);
    @PostMapping("/submit")
    public void postEvent(@RequestParam EventDTO eventDTO, BindingResult bindingResult);
}


