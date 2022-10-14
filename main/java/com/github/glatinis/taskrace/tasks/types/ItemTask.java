package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import org.bukkit.Material;

public class ItemTask extends Task {
    private Material item;

    public ItemTask(long taskDuration, Material item) {
        super(taskDuration);
        this.taskType = TaskTypes.ITEM;
        this.item = item;
    }

    public Material getItem() {
        return item;
    }
}
