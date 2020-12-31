package io.github.mmpodkanski.logic;

import io.github.mmpodkanski.TaskConfigurationProperties;
import io.github.mmpodkanski.model.ProjectRepository;
import io.github.mmpodkanski.model.TaskGroupRepository;
import io.github.mmpodkanski.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskGroupService service,
            final TaskConfigurationProperties config
    ) {
        return new ProjectService(repository, taskGroupRepository, service, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository
    ) {
        return new TaskGroupService(repository, taskRepository);
    }
}
