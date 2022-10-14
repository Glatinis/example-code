package com.github.glatinis.taskrace.listeners;

import com.github.glatinis.taskrace.TaskRace;
import com.github.glatinis.taskrace.managers.GameManager;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import com.github.glatinis.taskrace.tasks.types.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class TaskListeners implements Listener {

    @EventHandler
    public void advancementTaskChecker(PlayerAdvancementDoneEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!gm.getPointsMap().containsKey(e.getPlayer())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.ADVANCEMENT)) {
            Player plr = e.getPlayer();
            AdvancementTask advancementTask = (AdvancementTask) gm.getCurrentTask();
            if (e.getAdvancement().getKey().getKey().equals(advancementTask.getAdvancement())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void biomeTaskChecker(PlayerMoveEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!gm.getPointsMap().containsKey(e.getPlayer())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.BIOME)) {
            Player plr = e.getPlayer();
            BiomeTask biomeTask = (BiomeTask) gm.getCurrentTask();
            if (plr.getLocation().getBlock().getBiome().equals(biomeTask.getBiome())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void chatTaskChecker(AsyncPlayerChatEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!gm.getPointsMap().containsKey(e.getPlayer())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.CHAT)) {
            Player plr = e.getPlayer();
            ChatTask chatTask = (ChatTask) gm.getCurrentTask();

            if (e.getMessage().equals(chatTask.getMessage())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void effectTaskChecker(EntityPotionEffectEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!(e.getEntity() instanceof Player)) return;
        if (!gm.getPointsMap().containsKey((Player) e.getEntity())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.EFFECT)) {
            Player plr = (Player) e.getEntity();
            EffectTask effectTask = (EffectTask) gm.getCurrentTask();

            if (e.getNewEffect().getType().equals(effectTask.getEffectType())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void itemTaskChecker(EntityPickupItemEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!(e.getEntity() instanceof Player)) return;
        if (!gm.getPointsMap().containsKey((Player) e.getEntity())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.ITEM)) {
            Player plr = (Player) e.getEntity();
            ItemTask itemTask = (ItemTask) gm.getCurrentTask();

            if (e.getItem().getItemStack().getType().equals(itemTask.getItem())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void itemTaskChecker2(InventoryClickEvent e) {
        GameManager gm = TaskRace.getGameManager();
        Player plr = (Player) e.getWhoClicked();
        if (!gm.getPointsMap().containsKey(plr)) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.ITEM)) {
            ItemTask itemTask = (ItemTask) gm.getCurrentTask();

            if (e.getClickedInventory().getType() != InventoryType.PLAYER) return;

            boolean hasItem = e.getCurrentItem().getType() == itemTask.getItem();
            boolean hadItem = e.getCursor().getType() == itemTask.getItem();

            if (hasItem || hadItem) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void killTaskChecker(EntityDeathEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!gm.getPointsMap().containsKey(e.getEntity().getKiller())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.KILL)) {
            Player plr = e.getEntity().getKiller();
            KillTask killTask = (KillTask) gm.getCurrentTask();

            if (e.getEntity().getType().equals(killTask.getEntityType())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void punchTaskChecker(EntityDamageByEntityEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!(e.getDamager() instanceof Player)) return;
        if (!gm.getPointsMap().containsKey((Player) e.getDamager())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.PUNCH)) {
            Player plr = (Player) e.getDamager();
            PunchTask punchTask = (PunchTask) gm.getCurrentTask();

            if (plr.getEquipment().getItemInMainHand().getType() != Material.AIR) return;
            if (e.getEntity().getType().equals(punchTask.getEntityType())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void deathTaskChecker(PlayerDeathEvent e) {
        GameManager gm = TaskRace.getGameManager();

        if (!gm.getPointsMap().containsKey(e.getEntity())) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.DEATH)) {
            Player plr = e.getEntity();
            DeathTask deathTask = (DeathTask) gm.getCurrentTask();

            if (e.getDeathMessage().contains(deathTask.getRequiredMessage())) {
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }
}
