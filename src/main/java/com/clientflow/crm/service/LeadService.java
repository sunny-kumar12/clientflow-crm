package com.clientflow.crm.service;

import com.clientflow.crm.dto.LeadForm;
import com.clientflow.crm.entity.Lead;
import com.clientflow.crm.entity.Lead.LeadStatus;
import com.clientflow.crm.repository.LeadRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional(readOnly = true)
    public Page<Lead> search(
            String keyword,
            LeadStatus status,
            Pageable pageable) {

        String normalized = (keyword == null || keyword.isBlank())
                ? null
                : keyword.trim();

        return leadRepository.searchLeads(
                normalized,
                status,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public List<Lead> findAll() {
        return leadRepository.findAll()
                .stream()
                .filter(lead -> !lead.isArchived())
                .toList();
    }

    @Transactional(readOnly = true)
    public Lead getById(Long id) {
        return leadRepository.findByIdAndArchivedFalse(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Lead not found: " + id
                        )
                );
    }

    @Transactional
    public Lead create(LeadForm form) {
        Lead lead = new Lead();

        copyForm(form, lead);

        return leadRepository.save(lead);
    }

    @Transactional
    public Lead update(Long id, LeadForm form) {
        Lead lead = getById(id);

        copyForm(form, lead);

        return leadRepository.save(lead);
    }

    @Transactional
    public void archive(Long id) {
        Lead lead = getById(id);

        lead.setArchived(true);

        leadRepository.save(lead);
    }

    private void copyForm(LeadForm form, Lead lead) {
        lead.setName(form.getName().trim());
        lead.setCompany(clean(form.getCompany()));
        lead.setEmail(clean(form.getEmail()));
        lead.setPhone(clean(form.getPhone()));
        lead.setSource(clean(form.getSource()));
        lead.setStatus(form.getStatus());
        lead.setEstimatedValue(form.getEstimatedValue());
        lead.setNotes(clean(form.getNotes()));
    }

    private String clean(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}