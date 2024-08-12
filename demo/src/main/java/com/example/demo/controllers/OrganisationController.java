package com.example.demo.controllers;

import com.example.demo.Services.AuthService;
import com.example.demo.Services.OrganisationService;
import com.example.demo.clients.ManagerClient;
import com.example.demo.clients.OrganisationClient;
import com.example.demo.dtos.OrganisationDTO;
import com.example.demo.enums.OrganisationPermission;
import com.example.demo.models.Organisation;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/organisation")
public class OrganisationController {
    AuthService authService;
    OrganisationClient organisationClient;
    OrganisationService organisationService;
    ManagerClient managerClient;

    @Autowired
    public OrganisationController(AuthService authService, OrganisationClient organisationClient, OrganisationService organisationService, ManagerClient managerClient) {
        this.authService = authService;
        this.organisationClient = organisationClient;
        this.organisationService = organisationService;
        this.managerClient = managerClient;
    }
    @GetMapping("/info")
    public String getOrganisationInfo(Model model){
        authService.getRoles(model);
        Organisation organisation = managerClient.findOrganisationByManager().getBody();
        if(organisation == null){
            return "redirect:/organisation/add";
        }
        return "organisation/organisation-info";
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
            if (!organisationClient.postOrganisation(organisationDTO)) {
                return "organisation/organisation-form";
            }
            return "redirect:/home";
            //some fucking thank you message
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
    public String postUpdate(@RequestParam("id") Integer id, @ModelAttribute OrganisationDTO organisationDTO) {
        organisationClient.postUpdatedOrganisation(organisationDTO);
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
    @GetMapping("/search/admin")
    public String getFilteredEventsForAdmin(Model model) {
        try {
            List<Organisation> organisations = organisationClient.getAllWaitingOrganisations(null, OrganisationPermission.WAITING);
            authService.getRoles(model);
            model.addAttribute("allOrganisations", organisations);
            return "organisation/waiting-organisations";
        } catch (FeignException.Forbidden e) {
            return "redirect:/authentication/login";
        }
    }
    @PostMapping("/accept")
    public String acceptEvent(@RequestParam(name = "id") Integer id){
        organisationClient.acceptOrganisation(id);
        return "redirect:/organisation/search/admin";
    }
    @PostMapping("/reject")
    public String rejectEvent(@RequestParam(name = "id") Integer id){
        organisationClient.rejectOrganisation(id);
        return "redirect:/organisation/search/admin";
    }

}
