package com.example.demo.controllers;

import com.example.demo.Services.OrganisationService;
import com.example.demo.clients.OrganisationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organisation")
public class OrganisationController {
    OrganisationClient organisationClient;
    OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationClient organisationClient, OrganisationService organisationService) {
        this.organisationClient = organisationClient;
        this.organisationService = organisationService;
    }

    @GetMapping("/all")
    public String allOrganisations(Model model) {
        model.addAttribute("allOrganisations", organisationService.allOrganisations());
        return "organisation/all-organisations";
    }
}
