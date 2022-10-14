package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import org.bukkit.advancement.Advancement;

public class AdvancementTask extends Task {
    private String advancement;
    private String name;

    public AdvancementTask(long taskDuration, String advancement, String name) {
        super(taskDuration);
        this.taskType = TaskTypes.ADVANCEMENT;
        this.advancement = advancement;
        this.name = name;
    }

    public String getAdvancement() {
        return advancement;
    }
    public String getName() {
        return name;
    }
}
