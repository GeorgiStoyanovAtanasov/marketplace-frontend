package com.example.demo.controllers;

import ch.qos.logback.core.model.Model;
import com.example.demo.dtos.EventTypeDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/event-type")
public class EventTypeController {
    @GetMapping("/add")
    public void addEventType(Model model) {

    }

    @PostMapping("/add")
    public void postEventType(@Valid @ModelAttribute EventTypeDTO eventTypeDTO) {

    }

    @GetMapping("/all")
    public String allEventsTypes() {
        return "event-type/all-event-types";
    }

    @DeleteMapping("/delete")
    public void deleteEventType(@RequestParam("id") Integer id) {

    }

    @PutMapping("/update")
    public void updateEventType(@RequestParam("id") Integer id, @RequestBody EventTypeDTO eventTypeDTO) {


    }
}

