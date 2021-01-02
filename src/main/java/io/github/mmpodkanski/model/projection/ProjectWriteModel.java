package io.github.mmpodkanski.model.projection;

import io.github.mmpodkanski.model.Project;
import io.github.mmpodkanski.model.ProjectSteps;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @Valid
    private List<ProjectSteps> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectSteps> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectSteps> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
