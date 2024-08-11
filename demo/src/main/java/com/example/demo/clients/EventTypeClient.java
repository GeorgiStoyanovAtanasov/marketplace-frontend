package com.example.demo.clients;

import com.example.demo.IndividualFeignConfig;
import com.example.demo.dtos.EventTypeDTO;
import com.example.demo.models.Organisation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "event-type-service", url = "${event-type-service.url}", configuration = IndividualFeignConfig.class)
public interface EventTypeClient {
    @PostMapping("/submit")
    void postEventType(@RequestBody EventTypeDTO eventTypeDTO);

    @GetMapping("/all")
    ResponseEntity<Iterable<EventTypeDTO>> allEventTypes();

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteEventType(@RequestParam("id") Integer id);

    @PutMapping("/update")
    void updateEventType(@RequestParam("id") Integer id, @RequestBody EventTypeDTO eventTypeDTO);
}
