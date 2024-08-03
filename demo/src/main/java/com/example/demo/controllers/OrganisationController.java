package com.example.demo.controllers;
import com.example.demo.Services.AuthService;
import com.example.demo.Services.OrganisationService;
import com.example.demo.clients.OrganisationClient;
import com.example.demo.dtos.OrganisationDTO;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/organisation")
public class OrganisationController {
    AuthService authService;
    OrganisationClient organisationClient;
    OrganisationService organisationService;
    @Autowired
    public OrganisationController(AuthService authService, OrganisationClient organisationClient, OrganisationService organisationService) {
        this.authService = authService;
        this.organisationClient = organisationClient;
        this.organisationService = organisationService;
    }
    @GetMapping("/add")
    public String addOrganisation(Model model) {
        authService.getRoles(model);
        model.addAttribute("organisationDTO", new OrganisationDTO());
        return "organisation/organisation-form";
    }
    @PostMapping("/submit")
    public String postOrganisation(@Valid @ModelAttribute OrganisationDTO organisationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "organisation/organisation-form";
        } else {
            organisationClient.postOrganisation(organisationDTO);
            return "redirect:/events";
        }
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("updateOrganisation", new OrganisationDTO());
        model.addAttribute("organisationId", id);
        authService.getRoles(model);
        return "organisation/organisation-update-form";
    }

    @PostMapping("/update")
    public String postUpdate(@RequestParam("id")Integer id,@ModelAttribute OrganisationDTO organisationDTO){
        organisationClient.postUpdatedOrganisation(id, organisationDTO);
        return "redirect:/organisation/all";
    }

    @GetMapping("/all")
    public String allOrganisations(Model model) {
            authService.getRoles(model);
            model.addAttribute("allOrganisations", organisationService.allOrganisations());
            return "organisation/all-organisations";
        }
        @PostMapping("/delete/{id}")
        public String deleteOrgById(@PathVariable Integer id) {
            try {
                organisationClient.deleteOrganisation(id);
                return "redirect:/organisation/all";
            } catch (FeignException.Forbidden e) {
                return "redirect:/authentication/login";
            }
        }
    }