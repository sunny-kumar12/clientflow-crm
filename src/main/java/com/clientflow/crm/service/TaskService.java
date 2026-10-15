package com.clientflow.crm.service;

import com.clientflow.crm.entity.Task;
import com.clientflow.crm.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAllByOrderByDueDateAsc();
    }

    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Task not found: " + id
                        )
                );
    }

    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}