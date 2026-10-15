
package com.clientflow.crm.controller;

import com.clientflow.crm.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardService.DashboardData data =
                dashboardService.getDashboardData();

        model.addAttribute("totalCustomers", data.totalCustomers());
        model.addAttribute("activeCustomers", data.activeCustomers());
        model.addAttribute("inactiveCustomers", data.inactiveCustomers());
        model.addAttribute(
                "newCustomersThisMonth",
                data.newCustomersThisMonth()
        );
        model.addAttribute("recentCustomers", data.recentCustomers());

        return "dashboard";
    }
}
