package com.clientflow.crm.repository;

import com.clientflow.crm.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByDueDateAsc();

}