package io.github.mmpodkanski.model.projection;

import io.github.mmpodkanski.model.Task;
import io.github.mmpodkanski.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private String description;
    /**
     * Deadline from the latest task in group.
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        description = source.getDescription();
        source.getTasks().stream()
                .map(task1 -> task1.getDeadline())
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);
        tasks = source.getTasks().stream()
                .map(task -> new GroupTaskReadModel(task))
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
