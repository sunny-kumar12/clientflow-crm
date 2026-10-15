package com.clientflow.crm.service;

import com.clientflow.crm.entity.Interaction;
import com.clientflow.crm.repository.InteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InteractionService {

    private final InteractionRepository interactionRepository;

    public InteractionService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Transactional(readOnly = true)
    public List<Interaction> findAll() {
        return interactionRepository.findAllByOrderByInteractionDateDesc();
    }

    @Transactional(readOnly = true)
    public Interaction getById(Long id) {
        return interactionRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Interaction not found: " + id)
                );
    }

    @Transactional
    public Interaction save(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    @Transactional
    public void delete(Long id) {
        interactionRepository.deleteById(id);
    }
}