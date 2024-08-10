package com.example.demo.dtos;


import com.example.demo.EventPermission.EventPermission;
import com.example.demo.EventStatus;
import com.example.demo.models.Organisation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class EventDTO {
    private Integer id;
    @NotEmpty(message = "The name of the event can not be empty!")
    private String name;
    private String date;
    @NotNull(message = "Please enter the duration of the event!")
    private int duration;
    @NotEmpty(message = "Please enter description!")
    private String description;
    private String image;
    @NotEmpty(message = "Please enter the place of the event!")
    private String place;
    @NotEmpty(message = "Please enter the start time of the event!")
    private String time;
    @NotNull(message = "Please enter the price of the ticket for the event!")
    private double ticketPrice;
    @NotNull(message = "Please enter the capacity of the event!")
    private int capacity;
    @JsonIgnore
    private MultipartFile file;
    private Organisation organisation;
    private EventTypeDTO eventTypeDTO;
    private EventStatus eventStatus;
    private List<UserDTO>users;
    private EventPermission eventPermission;

}

