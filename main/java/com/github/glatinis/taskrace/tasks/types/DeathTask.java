package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;

public class DeathTask extends Task {
    private String requiredMessage;

    public DeathTask(long taskDuration, String requiredMessage) {
        super(taskDuration);
        this.taskType = TaskTypes.DEATH;
        this.requiredMessage = requiredMessage;
    }

    public String getRequiredMessage() {
        return requiredMessage;
    }
}
