package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import org.bukkit.potion.PotionEffectType;

public class EffectTask extends Task {
    private PotionEffectType effectType;

    public EffectTask(long taskDuration, PotionEffectType effectType) {
        super(taskDuration);
        this.taskType = TaskTypes.EFFECT;
        this.effectType = effectType;
    }

    public PotionEffectType getEffectType() {
        return effectType;
    }
}
