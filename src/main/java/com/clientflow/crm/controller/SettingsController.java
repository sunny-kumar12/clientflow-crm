package com.clientflow.crm.controller;

import com.clientflow.crm.dto.SettingsForm;
import com.clientflow.crm.entity.AppUser;
import com.clientflow.crm.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public String settings(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        AppUser user = settingsService.getCurrentUser(userDetails);

        model.addAttribute("user", user);
        model.addAttribute("settingsForm", new SettingsForm());

        return "settings/index";
    }

    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid SettingsForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        AppUser user = settingsService.getCurrentUser(userDetails);

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "settings/index";
        }

        try {

            settingsService.changePassword(
                    userDetails,
                    form
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Password changed successfully."
            );

            return "redirect:/settings";

        } catch (IllegalArgumentException ex) {

            model.addAttribute("user", user);
            model.addAttribute(
                    "errorMessage",
                    ex.getMessage()
            );

            return "settings/index";
        }
    }
}