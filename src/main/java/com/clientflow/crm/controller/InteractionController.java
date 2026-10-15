package com.clientflow.crm.controller;

import com.clientflow.crm.entity.Customer;
import com.clientflow.crm.entity.Interaction;
import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.service.CustomerService;
import com.clientflow.crm.service.InteractionService;
import com.clientflow.crm.service.LeadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/interactions")
public class InteractionController {

    private final InteractionService interactionService;
    private final CustomerService customerService;
    private final LeadService leadService;

    public InteractionController(
            InteractionService interactionService,
            CustomerService customerService,
            LeadService leadService) {

        this.interactionService = interactionService;
        this.customerService = customerService;
        this.leadService = leadService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "interactions",
                interactionService.findAll()
        );

        return "interactions/list";
    }

    @GetMapping("/new")
    public String newInteraction(Model model) {

        model.addAttribute(
                "interaction",
                new Interaction()
        );

        model.addAttribute(
                "customers",
                customerService.findAll()
        );

        model.addAttribute(
                "leads",
                leadService.findAll()
        );

        model.addAttribute(
                "types",
                Interaction.InteractionType.values()
        );

        model.addAttribute(
                "pageTitle",
                "Record Interaction"
        );

        return "interactions/form";
    }

    @PostMapping
    public String create(
            @ModelAttribute("interaction") Interaction interaction,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId,
            RedirectAttributes redirectAttributes) {

        if (customerId != null) {
            Customer customer = customerService.getCustomer(customerId);
            interaction.setCustomer(customer);
        }

        if (leadId != null) {
            Lead lead = leadService.getById(leadId);
            interaction.setLead(lead);
        }

        interactionService.save(interaction);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Interaction recorded successfully."
        );

        return "redirect:/interactions";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        interactionService.delete(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Interaction deleted successfully."
        );

        return "redirect:/interactions";
    }
}