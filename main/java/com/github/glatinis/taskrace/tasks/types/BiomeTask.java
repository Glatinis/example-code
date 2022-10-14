package com.github.glatinis.taskrace.tasks.types;

import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import org.bukkit.block.Biome;

public class BiomeTask extends Task {
    private Biome biome;

    public BiomeTask(long taskDuration, Biome biome) {
        super(taskDuration);
        this.taskType = TaskTypes.BIOME;
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }
}
