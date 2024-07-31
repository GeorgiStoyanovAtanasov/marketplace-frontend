package com.example.demo.Services;

import com.example.demo.clients.OrganisationClient;
import com.example.demo.dtos.EventDTO;
import com.example.demo.models.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrganisationService {
    OrganisationClient organisationClient;

    @Autowired
    public OrganisationService(OrganisationClient organisationClient) {
        this.organisationClient = organisationClient;
    }

    public Iterable<Organisation> allOrganisations() {
        try {
            Iterable<Organisation> response = organisationClient.allOrganisations().getBody();
            return response;
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching events from API", e);
        }
    }
}
