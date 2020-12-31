package io.github.mmpodkanski.model.projection;

import io.github.mmpodkanski.model.Task;

public class GroupTaskReadModel {
    private String description;
    private boolean done;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }
}
