package com.github.glatinis.taskrace.listeners;

import com.github.glatinis.taskrace.TaskRace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BarListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!TaskRace.getPlugin().bar.getPlayers().contains(e.getPlayer())) {
            TaskRace.getPlugin().bar.addPlayer(e.getPlayer());
        }
    }
}
