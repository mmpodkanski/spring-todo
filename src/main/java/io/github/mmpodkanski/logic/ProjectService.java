package io.github.mmpodkanski.logic;

import io.github.mmpodkanski.TaskConfigurationProperties;
import io.github.mmpodkanski.model.Project;
import io.github.mmpodkanski.model.ProjectRepository;
import io.github.mmpodkanski.model.TaskGroupRepository;
import io.github.mmpodkanski.model.projection.GroupReadModel;
import io.github.mmpodkanski.model.projection.GroupTaskWriteModel;
import io.github.mmpodkanski.model.projection.GroupWriteModel;
import io.github.mmpodkanski.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService taskGruopService;


    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskGroupService taskGruopService, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGruopService = taskGruopService;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                    }
                            ).collect(Collectors.toSet())
                    );
                    return taskGruopService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
