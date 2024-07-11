package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "event-service", url = "http://localhost:8085/event")
public interface EventClient {
    @GetMapping("/all")
    ResponseEntity<Map<String, List<?>>> getEventsAndTypes();
}


