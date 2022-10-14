package com.github.glatinis.taskrace.managers;

import com.github.glatinis.taskrace.TaskRace;
import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import com.github.glatinis.taskrace.tasks.types.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private HashMap<Task, Integer> playedCount = new HashMap<>();

    public TaskManager() {
        loadTasks();
    }

    private void loadTasks() {
        // 30 Sec
        tasks.add(new ItemTask(30, Material.WHEAT_SEEDS));
        tasks.add(new KillTask(30, EntityType.PIG));
        tasks.add(new KillTask(30, EntityType.COW));
        tasks.add(new ItemTask(30, Material.DIRT));
        tasks.add(new OtherTask(30, "Crouch. Nothing else"));
//        tasks.add(new OtherTask(30, "Empty your inventory"));
        tasks.add(new PunchTask(30, EntityType.PIG));
        tasks.add(new PunchTask(30, EntityType.COW));
        tasks.add(new ItemTask(30, Material.SUGAR));

        // 60 Sec
        tasks.add(new DeathTask(60, "hit the ground too hard"));
        tasks.add(new ItemTask(60, Material.BREAD));
        tasks.add(new OtherTask(60, "Swim in some water"));
        tasks.add(new PunchTask(60, EntityType.PLAYER));
        tasks.add(new BiomeTask(60, Biome.PLAINS));
        tasks.add(new BiomeTask(60, Biome.FOREST));
        tasks.add(new ItemTask(60, Material.LEATHER));
        tasks.add(new ChatTask(60, "gdlfik3werf"));
        tasks.add(new ChatTask(60, "gf5efds"));
        tasks.add(new ChatTask(60, "Subscribe to Beppo"));
        tasks.add(new OtherTask(60, "Kill a baby mob"));
        tasks.add(new OtherTask(60, "Hold bones in both hands"));

        // 3 Min
        tasks.add(new DeathTask(180, "burned to death"));
        tasks.add(new DeathTask(180, "tried to swim in lava"));
        tasks.add(new ItemTask(180, Material.DIAMOND));
        tasks.add(new KillTask(180, EntityType.GOAT));
        tasks.add(new KillTask(180, EntityType.BAT));
        tasks.add(new KillTask(180, EntityType.DONKEY));
        tasks.add(new OtherTask(180, "Reach half a heart"));
        tasks.add(new OtherTask(180, "Punch a mob to death"));
        tasks.add(new OtherTask(180, "Feed a cow"));
        tasks.add(new BiomeTask(180, Biome.DARK_FOREST));
        tasks.add(new BiomeTask(180, Biome.DESERT));
        tasks.add(new ItemTask(180, Material.BRICK));
        tasks.add(new ItemTask(180, Material.GLASS_BOTTLE));
        tasks.add(new ItemTask(180, Material.HAY_BLOCK));
        tasks.add(new OtherTask(180, "Breed any animal"));
        tasks.add(new OtherTask(180, "Tame a horse"));
        tasks.add(new OtherTask(180, "Push a pig off a cliff"));
        tasks.add(new OtherTask(180, "Cook a piece of porkchop"));
        tasks.add(new OtherTask(180, "Breed pigs together"));
        tasks.add(new ItemTask(180, Material.GRASS));

        // 4 Min
        tasks.add(new OtherTask(240, "Shoot off a firework"));
        tasks.add(new ItemTask(240, Material.LIGHTNING_ROD));
        tasks.add(new DeathTask(240, "discovered the floor was lava"));
        tasks.add(new DeathTask(240, "was stung to death"));
        tasks.add(new OtherTask(240, "Climb a mountain"));
        // structure village here
        tasks.add(new KillTask(240, EntityType.TURTLE));
        tasks.add(new EffectTask(240, PotionEffectType.DOLPHINS_GRACE));
        tasks.add(new KillTask(240, EntityType.PLAYER));
//        tasks.add(new AdvancementTask(240, "story/enter_the_nether", "We Need To Go Deeper"));
        tasks.add(new KillTask(240, EntityType.VILLAGER));
        tasks.add(new ItemTask(240, Material.CAULDRON));
        tasks.add(new ItemTask(240, Material.FILLED_MAP));
        tasks.add(new ItemTask(240, Material.CHAIN));
        tasks.add(new ItemTask(240, Material.GREEN_BED));
        tasks.add(new ItemTask(240, Material.CYAN_BED));
        tasks.add(new ItemTask(240, Material.BLUE_BED));
        tasks.add(new ItemTask(240, Material.PINK_BED));
        tasks.add(new OtherTask(240, "Reach bedrock"));
        tasks.add(new OtherTask(240, "Capture an axolotl"));
        tasks.add(new OtherTask(240, "Cook meat on campfire"));
        tasks.add(new OtherTask(240, "Wake a villager"));

        // 5 Min
        tasks.add(new DeathTask(300, "was impaled on a stalagmite"));
        tasks.add(new DeathTask(300, "was skewered by a falling stalactite"));
        tasks.add(new DeathTask(300, "froze to death"));
        tasks.add(new KillTask(300, EntityType.GLOW_SQUID));
        tasks.add(new BiomeTask(300, Biome.DRIPSTONE_CAVES));
        tasks.add(new OtherTask(300, "Throw an enderpearl"));
        // structure shipwreck here
        tasks.add(new DeathTask(300, "Warden"));
        tasks.add(new BiomeTask(300, Biome.OCEAN));
        tasks.add(new ItemTask(300, Material.AMETHYST_SHARD));
        tasks.add(new ItemTask(300, Material.BELL));
        tasks.add(new OtherTask(300, "Kill yourself with a bow and arrow"));
        tasks.add(new OtherTask(300, "Kill three villagers"));
        tasks.add(new ItemTask(300, Material.CLOCK));
        tasks.add(new PunchTask(300, EntityType.ZOMBIFIED_PIGLIN));
        tasks.add(new ItemTask(300, Material.GRASS_BLOCK));

        // 6 Min
        tasks.add(new DeathTask(360, "was squashed by a falling anvil"));
        tasks.add(new BiomeTask(360, Biome.LUSH_CAVES));
        tasks.add(new ItemTask(360, Material.DIAMOND_HOE));
        tasks.add(new KillTask(360, EntityType.AXOLOTL));
        tasks.add(new EffectTask(360, PotionEffectType.WATER_BREATHING));
        tasks.add(new OtherTask(360, "Eat suspicious stew"));
        tasks.add(new KillTask(360, EntityType.SLIME));
        tasks.add(new KillTask(360, EntityType.STRIDER));
        tasks.add(new ItemTask(360, Material.CANDLE));
        tasks.add(new OtherTask(360, "Make a snow golem"));
        tasks.add(new ItemTask(360, Material.POWDER_SNOW_BUCKET));
        tasks.add(new ItemTask(360, Material.GLOW_ITEM_FRAME));
        tasks.add(new ItemTask(360, Material.COBWEB));
        tasks.add(new ItemTask(360, Material.ACTIVATOR_RAIL));
        tasks.add(new PunchTask(360, EntityType.PLAYER));
        tasks.add(new OtherTask(360, "Go to coordinates"));
        tasks.add(new OtherTask(360, "Eat pufferfish"));
//        tasks.add(new OtherTask(360, "Get three leaf variants"));
        tasks.add(new OtherTask(360, "Pot a cactus"));

        // 8 Min
        tasks.add(new DeathTask(480, "was squished too much"));
        tasks.add(new ItemTask(480, Material.EMERALD));
        tasks.add(new ItemTask(480, Material.ROOTED_DIRT));
        tasks.add(new OtherTask(480, "Glow a sign"));
        tasks.add(new OtherTask(480, "Trade with a piglin"));
        tasks.add(new OtherTask(480, "Shoot a target block"));
        tasks.add(new ItemTask(480, Material.AXOLOTL_BUCKET));
        // structure mineshaft
        // structure pillager outpost
        tasks.add(new ItemTask(480, Material.TINTED_GLASS));
        tasks.add(new OtherTask(480, "Make a iron golem"));
        tasks.add(new ItemTask(480, Material.HONEY_BOTTLE));
        tasks.add(new OtherTask(480, "Fall through dripleaf"));
        tasks.add(new BiomeTask(480, Biome.SOUL_SAND_VALLEY));
        tasks.add(new ItemTask(480, Material.SEA_PICKLE));
//        tasks.add(new AdvancementTask(480, "story/enchant_item", "Enchanter"));
        tasks.add(new ItemTask(480, Material.BLAZE_ROD));
//        tasks.add(new OtherTask(480, "Reach the world border"));
        tasks.add(new OtherTask(480, "Enter five unique biomes"));

        // 9 Min
        tasks.add(new OtherTask(540, "Manhunt!"));
        tasks.add(new ItemTask(540, Material.DIAMOND_BLOCK));
        tasks.add(new ItemTask(540, Material.CAKE));
        // structure bastion remnant
        tasks.add(new OtherTask(540, "Break a spawner block"));
        tasks.add(new OtherTask(540, "Reach XP level 20"));
        tasks.add(new ItemTask(540, Material.SPYGLASS));
//        tasks.add(new AdvancementTask(540, "adventure/fall_from_world_height", "Cave and Cliffs"));
        // structure ancient city
        tasks.add(new ItemTask(540, Material.SHROOMLIGHT));
        tasks.add(new OtherTask(5, "Don't run"));
        tasks.add(new OtherTask(60, "Most bells rung in a minute"));
        tasks.add(new OtherTask(300, "Return to spawn"));
        tasks.add(new OtherTask(20, "Travel furthest in time given"));
        tasks.add(new OtherTask(120, "Most fish caught in time given"));

        Collections.shuffle(tasks);

        for (Task task : tasks) {
            playedCount.put(task, 0);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getRandomTask() {
//        return new ItemTask(480, Material.HONEY_BOTTLE);
        return tasks.get(new Random().nextInt(tasks.size()));
    }

    public Task getNextTask() {
        int leastPlayed = Collections.min(playedCount.values());

        for (Task task : getTasks()) {
            if (playedCount.get(task) <= leastPlayed) {
                playedCount.put(task, leastPlayed + 1);
                TaskRace.getPlugin().getLogger().info(String.valueOf(leastPlayed));
                return task;
            }
        }

        TaskRace.getPlugin().getLogger().severe("Could not find least played task.");
        return getRandomTask();
    }

    public static int getTaskValue(Task task, int points) {
        int toGive = points;
        if (task != null) {
            if (task.getTaskDuration() == 30) toGive = 1;
            if (task.getTaskDuration() == 60) toGive = 2;
            if (task.getTaskDuration() == 180) toGive = 3;
            if (task.getTaskDuration() == 240) toGive = 4;
            if (task.getTaskDuration() == 300) toGive = 5;
            if (task.getTaskDuration() == 360) toGive = 6;
            if (task.getTaskDuration() == 480) toGive = 7;
            if (task.getTaskDuration() == 540) toGive = 8;
        }

        return toGive;
    }

    public static String getValueString(Task task) {
        return ChatColor.GREEN + " (+" + getTaskValue(task, 3) + ")";
    }
}
