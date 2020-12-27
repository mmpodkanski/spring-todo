package io.github.mmpodkanski.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectSteps> steps;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;

    public Project() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    void setSteps(final Set<ProjectSteps> step) {
        this.steps = step;
    }

    public Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(final Set<TaskGroup> group) {
        this.groups = group;
    }

    public Set<ProjectSteps> getSteps() {
        return steps;
    }
}
