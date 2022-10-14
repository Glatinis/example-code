package com.github.glatinis.taskrace.tasks;

public abstract class Task {

    protected TaskTypes taskType;
    private final long taskDuration;

    protected Task(long taskDuration) {
        this.taskDuration = taskDuration;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public long getTaskDuration() {
        return taskDuration;
    }
}
