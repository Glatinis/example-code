package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;

public class OtherTask extends Task {

    private final String instruction;

    public OtherTask(long taskDuration, String instruction) {
        super(taskDuration);
        this.taskType = TaskTypes.OTHER;
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}
