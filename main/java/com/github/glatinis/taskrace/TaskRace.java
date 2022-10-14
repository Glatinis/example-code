package com.github.glatinis.taskrace;

import com.github.glatinis.taskrace.commands.TaskRaceCommand;
import com.github.glatinis.taskrace.completers.TaskRaceCompleter;
import com.github.glatinis.taskrace.listeners.BarListeners;
import com.github.glatinis.taskrace.listeners.OtherListeners;
import com.github.glatinis.taskrace.listeners.TaskListeners;
import com.github.glatinis.taskrace.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaskRace extends JavaPlugin {

    public static final String p = ChatColor.DARK_GRAY +"[" + ChatColor.BLUE + "TaskRace" + ChatColor.DARK_GRAY + "] ";
    private static TaskRace plugin;
    private static GameManager gameManager;
    public BossBar bar;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new TaskListeners(), this);
        getServer().getPluginManager().registerEvents(new OtherListeners(), this);
        getServer().getPluginManager().registerEvents(new BarListeners(), this);

        getCommand("taskrace").setExecutor(new TaskRaceCommand());
        getCommand("taskrace").setTabCompleter(new TaskRaceCompleter());

        if (bar == null)
            bar = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE + "-| TaskRace |-", BarColor.BLUE, BarStyle.SOLID);
        if (Bukkit.getOnlinePlayers().size() > 0)
            for (Player plr : Bukkit.getOnlinePlayers())
                bar.addPlayer(plr);

        Bukkit.getConsoleSender().sendMessage(p + ChatColor.GREEN + "Enabled successfully!");
    }

    @Override
    public void onDisable() {
        bar.setVisible(false);

        if (Bukkit.getScoreboardManager().getMainScoreboard().getObjective("scores") != null)
            Bukkit.getScoreboardManager().getMainScoreboard().getObjective("scores").unregister();

        Bukkit.getConsoleSender().sendMessage(p + ChatColor.RED + "Disabled successfully!");
    }

    public static TaskRace getPlugin() {
        return plugin;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
