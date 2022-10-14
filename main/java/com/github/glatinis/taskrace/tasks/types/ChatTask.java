package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;

public class ChatTask extends Task {
    private String message;

    public ChatTask(long taskDuration, String message) {
        super(taskDuration);
        this.taskType = TaskTypes.CHAT;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
