package com.clientflow.crm.controller;

import com.clientflow.crm.entity.Customer;
import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.entity.Task;
import com.clientflow.crm.service.CustomerService;
import com.clientflow.crm.service.LeadService;
import com.clientflow.crm.service.TaskService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CustomerService customerService;
    private final LeadService leadService;

    public TaskController(
            TaskService taskService,
            CustomerService customerService,
            LeadService leadService) {

        this.taskService = taskService;
        this.customerService = customerService;
        this.leadService = leadService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "tasks",
                taskService.findAll()
        );

        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTask(Model model) {

        prepareForm(model, new Task(), "Create Task");

        return "tasks/form";
    }

    @GetMapping("/{id}/edit")
    public String editTask(
            @PathVariable Long id,
            Model model) {

        Task task = taskService.getById(id);

        prepareForm(model, task, "Edit Task");

        return "tasks/form";
    }

    @PostMapping
    public String save(
            @ModelAttribute("task") Task task,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long leadId,
            RedirectAttributes redirectAttributes) {

        setRelations(task, customerId, leadId);

        boolean isNew = task.getId() == null;

        taskService.save(task);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                isNew
                        ? "Task created successfully."
                        : "Task updated successfully."
        );

        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        taskService.delete(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Task deleted successfully."
        );

        return "redirect:/tasks";
    }

    private void prepareForm(
            Model model,
            Task task,
            String pageTitle) {

        model.addAttribute("task", task);

        model.addAttribute(
                "customers",
                customerService.findAll()
        );

        model.addAttribute(
                "leads",
                leadService.findAll()
        );

        model.addAttribute(
                "statuses",
                Task.TaskStatus.values()
        );

        model.addAttribute(
                "priorities",
                Task.TaskPriority.values()
        );

        model.addAttribute(
                "pageTitle",
                pageTitle
        );
    }

    private void setRelations(
            Task task,
            Long customerId,
            Long leadId) {

        task.setCustomer(null);
        task.setLead(null);

        if (customerId != null) {

            Customer customer =
                    customerService.getCustomer(customerId);

            task.setCustomer(customer);
        }

        if (leadId != null) {

            Lead lead =
                    leadService.getById(leadId);

            task.setLead(lead);
        }
    }
}