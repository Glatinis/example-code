package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import org.bukkit.entity.EntityType;

public class PunchTask extends Task {
    private EntityType entityType;

    public PunchTask(long taskDuration, EntityType entityType) {
        super(taskDuration);
        this.taskType = TaskTypes.PUNCH;
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
