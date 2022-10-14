package com.github.glatinis.taskrace.utils;

import org.bukkit.World;

public class GameUtils {

    public static String getFriendlyName(String unfriendly) {
        return unfriendly.replace("_", " ").toLowerCase();
    }

    public static boolean isDay(World world) {
        long time = world.getTime();
        return time < 6000 || time > 23850;
    }
}
