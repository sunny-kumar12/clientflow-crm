
package com.clientflow.crm.controller;

import com.clientflow.crm.service.ReportsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/reports")
    public String reports(Model model) {

        ReportsService.ReportData reportData =
                reportsService.getReportData();

        model.addAttribute("report", reportData);

        return "reports";
    }
}
