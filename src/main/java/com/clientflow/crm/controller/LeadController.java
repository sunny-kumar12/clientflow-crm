
package com.clientflow.crm.controller;

import com.clientflow.crm.dto.LeadForm;
import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.entity.Lead.LeadStatus;
import com.clientflow.crm.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int safePage = Math.max(page, 0);

        Page<Lead> leads = leadService.search(
                keyword,
                status,
                PageRequest.of(
                        safePage, 10,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
        );

        model.addAttribute("leads", leads);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", LeadStatus.values());

        return "leads/list";
    }

    @GetMapping("/new")
    public String newLead(Model model) {
        model.addAttribute("leadForm", new LeadForm());
        model.addAttribute("statuses", LeadStatus.values());
        model.addAttribute("pageTitle", "Create Lead");
        model.addAttribute("formAction", "/leads");
        return "leads/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("leadForm") LeadForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("pageTitle", "Create Lead");
            model.addAttribute("formAction", "/leads");
            return "leads/form";
        }

        leadService.create(form);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Lead created successfully."
        );

        return "redirect:/leads";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Lead lead = leadService.getById(id);
        LeadForm form = new LeadForm();

        form.setName(lead.getName());
        form.setCompany(lead.getCompany());
        form.setEmail(lead.getEmail());
        form.setPhone(lead.getPhone());
        form.setStatus(lead.getStatus());
        form.setEstimatedValue(lead.getEstimatedValue());

        model.addAttribute("leadForm", form);
        model.addAttribute("statuses", LeadStatus.values());
        model.addAttribute("pageTitle", "Edit Lead");
        model.addAttribute("formAction", "/leads/" + id);
        model.addAttribute("leadId", id);

        return "leads/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("leadForm") LeadForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", LeadStatus.values());
            model.addAttribute("pageTitle", "Edit Lead");
            model.addAttribute("formAction", "/leads/" + id);
            model.addAttribute("leadId", id);
            return "leads/form";
        }

        leadService.update(id, form);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Lead updated successfully."
        );

        return "redirect:/leads";
    }

    @PostMapping("/{id}/archive")
    public String archive(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        leadService.archive(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Lead archived successfully."
        );

        return "redirect:/leads";
    }
}
