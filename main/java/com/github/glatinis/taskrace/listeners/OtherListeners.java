package com.github.glatinis.taskrace.listeners;

import com.github.glatinis.taskrace.TaskRace;
import com.github.glatinis.taskrace.managers.GameManager;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import com.github.glatinis.taskrace.tasks.types.OtherTask;
import com.github.glatinis.taskrace.utils.GameUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class OtherListeners implements Listener {

    private NamespacedKey punchKey = new NamespacedKey(TaskRace.getPlugin(), "punched");
    private HashMap<Player, Integer> villagerKills = new HashMap<>();
    private HashMap<Player, List<Biome>> visitedBiomes = new HashMap<>();

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        if (!e.getPlayer().isSneaking()) return;

        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Crouch. Nothing else")) {
                Player plr = e.getPlayer();
                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onSwim(PlayerMoveEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Swim in some water")) {
                Player plr = e.getPlayer();

                if (plr.getLocation().getBlock().getType().equals(Material.WATER)) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;

        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            if (e.getEntity().getKiller() == null) return;
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Kill a baby mob")) {
                if (!(e.getEntity() instanceof Ageable)) return;
                if (!(e.getEntity() instanceof Mob)) return;

                Ageable ageable = (Ageable) e.getEntity();
                if (ageable.isAdult()) return;

                Player plr = e.getEntity().getKiller();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Punch a mob to death")) {
                if (e.getEntity().getPersistentDataContainer().has(punchKey, PersistentDataType.INTEGER)) return;
                Player plr = e.getEntity().getKiller();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Push a pig off a cliff")) {
                if (e.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.FALL) return;
                if (!(e.getEntity() instanceof Pig)) return;
                Player plr = e.getEntity().getKiller();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Kill three villagers")) {
                if (e.getEntity().getType() != EntityType.VILLAGER) return;

                Player plr = e.getEntity().getKiller();
                if (!villagerKills.containsKey(plr)) {
                    villagerKills.put(plr, 0);
                }

                villagerKills.put(plr, villagerKills.get(plr) + 1);

                if (villagerKills.get(plr) >= 3) {
                    villagerKills = new HashMap<>();
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }
        }
    }

    @EventHandler
    public void onHandSwitch(PlayerSwapHandItemsEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Hold bones in both hands")) {
                Player plr = e.getPlayer();

                if (e.getMainHandItem().getType().equals(Material.BONE) &&
                    e.getOffHandItem().getType().equals(Material.BONE)) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }
        }
    }

    @EventHandler
    public void onHalfHeart(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Reach half a heart")) {
                Player plr = (Player) e.getEntity();

                if (!plr.isDead() && plr.getHealth() < 2) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }
        }
    }

    @EventHandler
    public void onPunchDeath(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Punch a mob to death")) {
                Player plr = (Player) e.getDamager();

                if (!plr.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
                    PersistentDataContainer data = e.getEntity().getPersistentDataContainer();
                    data.set(punchKey, PersistentDataType.INTEGER, 0);
                }
            }
        }
    }

    @EventHandler
    public void onCowFeed(PlayerInteractEntityEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Feed a cow")) {
                Player plr = e.getPlayer();

                if (!(e.getRightClicked() instanceof Cow)) return;
                if (!plr.getEquipment().getItemInMainHand().getType().equals(Material.WHEAT)) return;

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Capture an axolotl")) {
                Player plr = e.getPlayer();

                if (!(e.getRightClicked() instanceof Axolotl)) return;
                if (!plr.getEquipment().getItemInMainHand().getType().equals(Material.WATER_BUCKET)) return;

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Breed any animal")) {
                if (!(e.getBreeder() instanceof Player)) return;
                Player plr = (Player) e.getBreeder();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Breed pigs together")) {
                if (!(e.getBreeder() instanceof Player)) return;
                if (!(e.getEntity() instanceof Pig)) return;
                Player plr = (Player) e.getBreeder();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onHorseTame(EntityTameEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Tame a horse")) {
                if (e.getEntityType() != EntityType.HORSE) return;
                Player plr = (Player) e.getOwner();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onTakePork(InventoryClickEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Cook a piece of porkchop")) {
                if (e.getClickedInventory() == null) return;
                if (e.getCurrentItem() == null) return;
                if (e.getClickedInventory().getType() != InventoryType.FURNACE &&
                e.getClickedInventory().getType() != InventoryType.BLAST_FURNACE) return;
                if (e.getCurrentItem().getType() != Material.COOKED_PORKCHOP) return;
                Player plr = (Player) e.getWhoClicked();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Shoot off a firework")) {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                if (e.getPlayer().getEquipment().getItemInMainHand().getType() != Material.FIREWORK_ROCKET) return;

                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Throw an enderpearl")) {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
                if (e.getPlayer().getEquipment().getItemInMainHand().getType() != Material.ENDER_PEARL) return;

                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Pot a cactus")) {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
                if (e.getClickedBlock().getType() != Material.FLOWER_POT) return;
                if (e.getPlayer().getEquipment().getItemInMainHand().getType() != Material.CACTUS) return;

                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Glow a sign")) {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
                if (e.getClickedBlock().getType().name().contains("sign")) return;
                if (e.getPlayer().getEquipment().getItemInMainHand().getType() != Material.GLOW_INK_SAC) return;

                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Most bells rung in a minute")) {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                if (e.getClickedBlock().getType() != Material.BELL) return;

                Player plr = e.getPlayer();

                if (!gm.bellsMap.containsKey(plr)) {
                    gm.bellsMap.put(plr, 0);
                }

                gm.bellsMap.put(plr, gm.bellsMap.get(plr) + 1);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType().equals(TaskTypes.OTHER)) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Climb a mountain")) {
                Player plr = e.getPlayer();
                List<Biome> peaks = Arrays.asList(Biome.JAGGED_PEAKS, Biome.FROZEN_PEAKS, Biome.STONY_PEAKS);
                if (peaks.contains(plr.getLocation().getBlock().getBiome())) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            else if (task.getInstruction().equals("Enter five unique biomes")) {
                Player plr = e.getPlayer();

                if (!visitedBiomes.containsKey(plr)) {
                    visitedBiomes.put(plr, new ArrayList<>());
                }

                List<Biome> visited = visitedBiomes.get(plr);
                Biome currBiome = plr.getLocation().getBlock().getBiome();
                if (!visited.contains(currBiome)) {
                    visited.add(currBiome);
                    visitedBiomes.put(plr, visited);
                }

                if (visitedBiomes.get(plr).size() >= 5) {
                    visitedBiomes = new HashMap<>();

                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            else if (task.getInstruction().equals("Reach bedrock")) {
                Player plr = e.getPlayer();
                Block block = plr.getLocation().subtract(0, 1, 0).getBlock();

                if (block.getType() == Material.BEDROCK) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            else if (task.getInstruction().equals("Fall through dripleaf")) {
                Player plr = e.getPlayer();
                Block block = plr.getLocation().getBlock();

                if (block.getType() == Material.BIG_DRIPLEAF) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            else if (task.getInstruction().equals("Go to coordinates")) {
                Player plr = e.getPlayer();
                Location loc = plr.getLocation();

                Vector check = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                if (gm.coords != null) {
                    if (check.equals(gm.coords)) {
                        gm.addPoints(plr, GameManager.pointsPerTask);
                        gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                    }
                }
            }

            else if (task.getInstruction().equals("Return to spawn")) {
                Player plr = e.getPlayer();
                if (plr.getLocation().getBlock().equals(Bukkit.getWorlds().get(0).getSpawnLocation().getBlock())) {
                    if (gm.notAtSpawn.contains(plr)) {
                        gm.notAtSpawn.remove(plr);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Kill yourself with a bow and arrow")) {
                Player plr = e.getEntity();
                String required = plr.getName() + " was shot by " + plr.getName();

                if (required.equals(e.getDeathMessage())) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            if (task.getInstruction().equals("Manhunt!")) {
                Player plr = e.getEntity();

                if (plr.getUniqueId().equals(gm.hunted.getUniqueId())) {
                    if (plr.getKiller() != null && plr.getKiller() != plr) {
                        Player killer = plr.getKiller();
                        gm.addPoints(killer, GameManager.pointsPerTask);
                        gm.loadNextTask(ChatColor.YELLOW + killer.getName() + " has won the task!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Eat suspicious stew")) {
                Player plr = e.getPlayer();

                if (e.getItem().getType() == Material.SUSPICIOUS_STEW) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }

            else if (task.getInstruction().equals("Eat pufferfish")) {
                Player plr = e.getPlayer();

                if (e.getItem().getType() == Material.PUFFERFISH) {
                    gm.addPoints(plr, GameManager.pointsPerTask);
                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Make a snow golem")) {
                if (e.getBlockPlaced().getType() != Material.CARVED_PUMPKIN) return;
                if (e.getBlockPlaced().getLocation().subtract(0, 1, 0).getBlock().getType() != Material.SNOW_BLOCK) return;
                if (e.getBlockPlaced().getLocation().subtract(0, 2, 0).getBlock().getType() != Material.SNOW_BLOCK) return;
                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }

            else if (task.getInstruction().equals("Make a iron golem")) {
                if (e.getBlockPlaced().getType() != Material.CARVED_PUMPKIN) return;
                if (e.getBlockPlaced().getLocation().subtract(0, 1, 0).getBlock().getType() != Material.IRON_BLOCK) return;
                if (e.getBlockPlaced().getLocation().subtract(0, 2, 0).getBlock().getType() != Material.IRON_BLOCK) return;

                Material g1b1 = e.getBlockPlaced().getLocation().subtract(1, 1, 0).getBlock().getType();
                Material g1b2 = e.getBlockPlaced().getLocation().subtract(-1, 1, 0).getBlock().getType();
                Material g2b1 = e.getBlockPlaced().getLocation().subtract(0, 1, 1).getBlock().getType();
                Material g2b2 = e.getBlockPlaced().getLocation().subtract(0, 1, -1).getBlock().getType();
                boolean g1Valid = g1b1 == Material.IRON_BLOCK && g1b2 == Material.IRON_BLOCK;
                boolean g2Valid = g2b1 == Material.IRON_BLOCK && g2b2 == Material.IRON_BLOCK;

                if (!(g1Valid || g2Valid)) return;

                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Shoot a target block")) {
                if (e.getHitBlock().getType() != Material.TARGET) return;
                if (!(e.getEntity().getShooter() instanceof Player)) return;
                Player plr = (Player) e.getEntity().getShooter();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Break a spawner block")) {
                if (e.getBlock().getType() != Material.SPAWNER) return;
                Player plr = e.getPlayer();

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onXP(PlayerExpChangeEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Reach XP level 20")) {
                Player plr = e.getPlayer();
                if (plr.getExpToLevel() < 20) return;

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onBlockCook(BlockCookEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Cook meat on campfire")) {
                if (e.getBlock().getType() != Material.CAMPFIRE) return;
                if (!e.getResult().getType().name().contains("cooked")) return;

                ItemMeta meta = e.getResult().getItemMeta();
                meta.setLore(Collections.singletonList("cooked"));
                e.getResult().setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Cook meat on campfire")) {
                if (!(e.getEntity() instanceof Player)) return;
                Player plr = (Player) e.getEntity();

                if (!e.getItem().getItemStack().hasItemMeta()) return;
                if (!e.getItem().getItemStack().getItemMeta().hasLore()) return;
                if (!e.getItem().getItemStack().getItemMeta().getLore().get(0).equals("cooked")) return;

                gm.addPoints(plr, GameManager.pointsPerTask);
                gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
            }
        }
    }

    @EventHandler
    public void onPoseChange(EntityPoseChangeEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Wake a villager")) {
                if (!(e.getEntity() instanceof Villager)) return;
                if (e.getPose() != Pose.STANDING) return;
                if (GameUtils.isDay(e.getEntity().getWorld())) return;

                for (Entity entity : e.getEntity().getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof Player) {
                        Player plr = (Player) entity;

                        gm.addPoints(plr, GameManager.pointsPerTask);
                        gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        GameManager gm = TaskRace.getGameManager();
        if (gm.getCurrentTask() == null) return;
        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask task = (OtherTask) gm.getCurrentTask();
            if (task.getInstruction().equals("Most fish caught in time given")) {
                if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

                Player plr = e.getPlayer();
                if (!gm.fishesCounter.containsKey(plr)) {
                    gm.fishesCounter.put(plr, 0);
                }

                gm.fishesCounter.put(plr, gm.fishesCounter.get(plr) + 1);
            }
        }
    }


//    @EventHandler
//    public void onEmpty(PlayerDropItemEvent e) {
//        GameManager gm = TaskRace.getGameManager();
//        if (gm.getCurrentTask().getTaskType() == TaskTypes.OTHER) {
//            OtherTask task = (OtherTask) gm.getCurrentTask();
//            if (task.getInstruction().equals("Empty your inventory")) {
//                Player plr = e.getPlayer();
//                boolean isEmpty = true;
//
//                for (int i = 0; i < plr.getInventory().getSize(); i++) {
//                    ItemStack item = plr.getInventory().getItem(i);
////                    Bukkit.broadcastMessage(item.getType());
////                    if (!item.getType().equals(Material.AIR)) {
////                        isEmpty = false;
////                    }
//                }
//
//                if (isEmpty) {
//                    gm.addPoints(plr, GameManager.pointsPerTask);
//                    gm.loadNextTask(ChatColor.YELLOW + plr.getName() + " has won the task!");
//                }
//            }
//        }
//    }
}
