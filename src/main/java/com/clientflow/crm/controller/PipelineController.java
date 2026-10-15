
package com.clientflow.crm.controller;

import com.clientflow.crm.entity.Lead.LeadStatus;
import com.clientflow.crm.service.PipelineService;
import com.clientflow.crm.service.LeadService;
import com.clientflow.crm.repository.LeadRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pipeline")
public class PipelineController {

    private final PipelineService pipelineService;
    private final LeadService leadService;
    private final LeadRepository leadRepository;

    public PipelineController(
            PipelineService pipelineService,
            LeadService leadService,
            LeadRepository leadRepository) {
        this.pipelineService = pipelineService;
        this.leadService = leadService;
        this.leadRepository = leadRepository;
    }

    @GetMapping
    public String pipeline(Model model) {
        model.addAttribute(
                "pipeline",
                pipelineService.getPipelineData()
        );
        model.addAttribute("statuses", LeadStatus.values());

        return "pipeline/board";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam LeadStatus status,
            RedirectAttributes redirectAttributes) {

        var lead = leadRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Lead not found: " + id));

        lead.setStatus(status);
        leadRepository.save(lead);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Lead stage updated successfully."
        );

        return "redirect:/pipeline";
    }
}
