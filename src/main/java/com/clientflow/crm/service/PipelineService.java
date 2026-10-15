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
public class PipelineService {

    private final LeadRepository leadRepository;

    public PipelineService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional(readOnly = true)
    public PipelineData getPipelineData() {

        List<Lead> activeLeads = leadRepository.findAll()
                .stream()
                .filter(lead -> !lead.isArchived())
                .toList();

        Map<LeadStatus, Long> counts =
                new EnumMap<>(LeadStatus.class);

        Map<LeadStatus, BigDecimal> stageValues =
                new EnumMap<>(LeadStatus.class);

        Map<LeadStatus, List<Lead>> leadsByStatus =
                new EnumMap<>(LeadStatus.class);

        for (LeadStatus status : LeadStatus.values()) {

            List<Lead> stageLeads = activeLeads.stream()
                    .filter(lead -> lead.getStatus() == status)
                    .toList();

            BigDecimal stageValue = stageLeads.stream()
                    .map(Lead::getEstimatedValue)
                    .filter(value -> value != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            counts.put(status, (long) stageLeads.size());
            stageValues.put(status, stageValue);
            leadsByStatus.put(status, stageLeads);
        }

        List<Lead> openLeads = activeLeads.stream()
                .filter(lead -> lead.getStatus() != LeadStatus.WON)
                .filter(lead -> lead.getStatus() != LeadStatus.LOST)
                .toList();

        List<Lead> wonLeads = activeLeads.stream()
                .filter(lead -> lead.getStatus() == LeadStatus.WON)
                .toList();

        BigDecimal openValue = openLeads.stream()
                .map(Lead::getEstimatedValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal wonValue = wonLeads.stream()
                .map(Lead::getEstimatedValue)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long total = activeLeads.size();
        long wonCount = counts.get(LeadStatus.WON);
        long lostCount = counts.get(LeadStatus.LOST);
        long closed = wonCount + lostCount;

        double winRate = closed == 0
                ? 0.0
                : wonCount * 100.0 / closed;

        return new PipelineData(
                counts,
                stageValues,
                leadsByStatus,
                openLeads,
                openValue,
                wonValue,
                total,
                winRate
        );
    }

    public record PipelineData(
            Map<LeadStatus, Long> counts,
            Map<LeadStatus, BigDecimal> stageValues,
            Map<LeadStatus, List<Lead>> leadsByStatus,
            List<Lead> openLeads,
            BigDecimal openValue,
            BigDecimal wonValue,
            long totalLeads,
            double winRate
    ) {
    }
}
