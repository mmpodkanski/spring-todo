package io.github.mmpodkanski.logic;

import io.github.mmpodkanski.TaskConfigurationProperties;
import io.github.mmpodkanski.model.*;
import io.github.mmpodkanski.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;


    ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId) && !config.getTemplate().isAllowMultipleTasks()) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
//        var project = repository.findById(projectId).orElseThrow(() -> new IllegalArgumentException());
//
//        var taskGroup = new TaskGroup(project.getDescription(),,project);
//        return new GroupReadModel(taskGroup);
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectSteps -> new Task(
                                    projectSteps.getDescription(),
                                    deadline.plusDays(projectSteps.getDaysToDeadline()))
                            ).collect(Collectors.toSet())
                    );
                    return targetGroup;
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
