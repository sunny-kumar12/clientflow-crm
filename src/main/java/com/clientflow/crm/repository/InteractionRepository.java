package com.clientflow.crm.repository;

import com.clientflow.crm.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    List<Interaction> findAllByOrderByInteractionDateDesc();
}