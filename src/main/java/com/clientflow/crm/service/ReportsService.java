
package com.clientflow.crm.service;

import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.entity.Lead.LeadStatus;
import com.clientflow.crm.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsService {

    private final LeadRepository leadRepository;

    public ReportsService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional(readOnly = true)
    public ReportData getReportData() {

        List<Lead> activeLeads = leadRepository.findAll()
                .stream()
                .filter(lead -> !lead.isArchived())
                .toList();

        Map<LeadStatus, Long> statusCounts =
                new EnumMap<>(LeadStatus.class);

        Map<LeadStatus, BigDecimal> statusValues =
                new EnumMap<>(LeadStatus.class);

        for (LeadStatus status : LeadStatus.values()) {

            List<Lead> stageLeads = activeLeads.stream()
                    .filter(lead -> lead.getStatus() == status)
                    .toList();

            BigDecimal stageValue = stageLeads.stream()
                    .map(Lead::getEstimatedValue)
                    .filter(value -> value != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            statusCounts.put(status, (long) stageLeads.size());
            statusValues.put(status, stageValue);
        }

        long totalLeads = activeLeads.size();

        long wonDeals = statusCounts.get(LeadStatus.WON);
        long lostDeals = statusCounts.get(LeadStatus.LOST);
        long closedDeals = wonDeals + lostDeals;

        BigDecimal openPipelineValue = activeLeads.stream()
                .filter(lead -> lead.getStatus() != LeadStatus.WON)
                .filter(lead -> lead.getStatus() != LeadStatus.LOST)
                .map(Lead::getEstimatedValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal wonValue = statusValues.get(LeadStatus.WON);

        double winRate = closedDeals == 0
                ? 0.0
                : wonDeals * 100.0 / closedDeals;

        return new ReportData(
                totalLeads,
                wonDeals,
                lostDeals,
                openPipelineValue,
                wonValue,
                winRate,
                statusCounts,
                statusValues
        );
    }

    public record ReportData(
            long totalLeads,
            long wonDeals,
            long lostDeals,
            BigDecimal openPipelineValue,
            BigDecimal wonValue,
            double winRate,
            Map<LeadStatus, Long> statusCounts,
            Map<LeadStatus, BigDecimal> statusValues
    ) {
    }
}
