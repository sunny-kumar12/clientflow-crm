package com.clientflow.crm.controller;

import com.clientflow.crm.dto.CustomerForm;
import com.clientflow.crm.entity.Customer;
import com.clientflow.crm.service.CustomerService;
import com.clientflow.crm.service.CustomerService.DuplicateCustomerEmailException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Customer> customers = customerService.searchCustomers(
                keyword, status, pageable
        );

        model.addAttribute("customers", customers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses",
                new String[]{"ACTIVE", "PENDING", "INACTIVE"});

        return "customers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customerForm", new CustomerForm());
        model.addAttribute("statuses",
                new String[]{"ACTIVE", "PENDING", "INACTIVE"});

        return "customers/form";
    }

    @PostMapping
    public String createCustomer(
            @Valid @ModelAttribute("customerForm") CustomerForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses",
                    new String[]{"ACTIVE", "PENDING", "INACTIVE"});
            return "customers/form";
        }

        try {
            customerService.createCustomer(form);
        } catch (DuplicateCustomerEmailException ex) {
            result.rejectValue(
                    "email", "duplicate", "A customer with this email already exists."
            );
            model.addAttribute("statuses",
                    new String[]{"ACTIVE", "PENDING", "INACTIVE"});
            return "customers/form";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Customer created successfully."
        );

        return "redirect:/customers";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Customer customer = customerService.getCustomer(id);

        CustomerForm form = new CustomerForm();
        form.setName(customer.getName());
        form.setCompany(customer.getCompany());
        form.setEmail(customer.getEmail());
        form.setPhone(customer.getPhone());
        form.setStatus(customer.getStatus());

        model.addAttribute("customerForm", form);
        model.addAttribute("customerId", id);
        model.addAttribute("statuses",
                new String[]{"ACTIVE", "PENDING", "INACTIVE"});

        return "customers/form";
    }

    @PostMapping("/{id}")
    public String updateCustomer(
            @PathVariable Long id,
            @Valid @ModelAttribute("customerForm") CustomerForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("customerId", id);
            model.addAttribute("statuses",
                    new String[]{"ACTIVE", "PENDING", "INACTIVE"});
            return "customers/form";
        }

        try {
            customerService.updateCustomer(id, form);
        } catch (DuplicateCustomerEmailException ex) {
            result.rejectValue(
                    "email", "duplicate", "A customer with this email already exists."
            );
            model.addAttribute("customerId", id);
            model.addAttribute("statuses",
                    new String[]{"ACTIVE", "PENDING", "INACTIVE"});
            return "customers/form";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Customer updated successfully."
        );

        return "redirect:/customers";
    }

    @PostMapping("/{id}/archive")
    public String archiveCustomer(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        customerService.archiveCustomer(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Customer archived successfully."
        );

        return "redirect:/customers";
    }
}