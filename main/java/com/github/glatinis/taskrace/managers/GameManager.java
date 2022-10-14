package com.github.glatinis.taskrace.managers;

import com.github.glatinis.taskrace.TaskRace;
import com.github.glatinis.taskrace.tasks.Task;
import com.github.glatinis.taskrace.tasks.TaskTypes;
import com.github.glatinis.taskrace.tasks.types.*;
import com.github.glatinis.taskrace.utils.GameUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.glatinis.taskrace.TaskRace.*;

public class GameManager {

    private boolean isGameStarted = false;
    public static final int pointsPerTask = 5;
    private final ChatColor highlight = ChatColor.LIGHT_PURPLE;
    private final ChatColor white = ChatColor.WHITE;
    private TaskManager taskManager;
    private Task currentTask = null;
    private HashMap<Player, Integer> pointsMap = new HashMap<>();
    public HashMap<Player, Integer> bellsMap = new HashMap<>();
    public HashMap<Player, Integer> fishesCounter = new HashMap<>();
    public HashMap<Player, Location> startLocs = new HashMap<>();
    public List<Player> notAtSpawn = new ArrayList<>();
    public Player hunted = null;
    public Player farthestPlr = null;
    public double farthestDist = -1f;
    public Vector coords = null;
    public int scoreToWin = 50;

    public GameManager() {
        taskManager = new TaskManager();
        if (TaskRace.getPlugin().getConfig().contains("winscore")) {
            scoreToWin = TaskRace.getPlugin().getConfig().getInt("winscore");
        }
    }

    public void startGame() {
        if (isGameStarted) return;

        for (Player plr : Bukkit.getOnlinePlayers()) {
            pointsMap.put(plr, 0);
            plr.playSound(plr, Sound.ITEM_GOAT_HORN_SOUND_1, 1f, 1f);
        }

        Bukkit.broadcastMessage(p + ChatColor.GREEN + "The game has been started!");

        isGameStarted = true;
        loadNextTask();
        final long[] taskStartTime = {System.currentTimeMillis()};
        final Task[] lastTask = {currentTask};

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isGameStarted) {
                    this.cancel();
                    return;
                }

                float elapsed = (System.currentTimeMillis() - taskStartTime[0]) / 1000f;

                if (!lastTask[0].equals(currentTask)) {
                    taskStartTime[0] = System.currentTimeMillis();
                }

                if (getCurrentTask() != null) {
                    if (getCurrentTask().getTaskType() == TaskTypes.OTHER) {
                        if (hunted != null) {
                            OtherTask task = (OtherTask) getCurrentTask();
                            if (task.getInstruction().equals("Manhunt!")) {
                                for (Player plr : Bukkit.getOnlinePlayers()) {
                                    plr.setCompassTarget(hunted.getLocation());
                                }
                            }
                        }
                    }
                }

                if (elapsed >= currentTask.getTaskDuration()) {
                    boolean isSpecial = false;

                    if (currentTask != null) {
                        if (currentTask.getTaskType() == TaskTypes.OTHER) {
                            OtherTask ot = (OtherTask) currentTask;
                            if (ot.getInstruction().equals("Manhunt!")) {
                                if (hunted != null) {
                                    addPoints(hunted, GameManager.pointsPerTask);
                                    loadNextTask(ChatColor.YELLOW + hunted.getName() + " has won the task!");
                                    isSpecial = true;
                                }
                            }

                            else if (ot.getInstruction().equals("Don't run")) {
                                for (Player plr : Bukkit.getOnlinePlayers()) {
                                    if (plr.isSprinting()) {
                                        plr.sendMessage(ChatColor.RED + "You lose 9 points, oops.");
                                        plr.playSound(plr, Sound.ENTITY_CAT_DEATH, 1f, 1f);

                                        addPoints(plr, -9);
                                        updateScoreboard();
                                    }
                                }
                            }

                            else if (ot.getInstruction().equals("Most bells rung in a minute")) {
                                Player winner = bellsMap.entrySet().stream().max((entry1, entry2) ->
                                        entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

                                addPoints(winner, GameManager.pointsPerTask);
                                loadNextTask(ChatColor.YELLOW + winner.getName() + " has won the task!");
                                isSpecial = true;
                            }

                            else if (ot.getInstruction().equals("Return to spawn")) {
                                for (Player plr : notAtSpawn) {
                                    if (pointsMap.containsKey(plr)) {
                                        plr.sendMessage(ChatColor.RED + "You lose 9 points, oops.");
                                        plr.playSound(plr, Sound.ENTITY_CAT_DEATH, 1f, 1f);
                                        addPoints(plr, -10);
                                    }
                                }
                            }

                            else if (ot.getInstruction().equals("Travel furthest in time given")) {
                                for (Player plr : startLocs.keySet()) {
                                    double traveled = plr.getLocation().distance(startLocs.get(plr));
                                    if (traveled > farthestDist) {
                                        farthestPlr = plr;
                                        farthestDist = traveled;
                                    }
                                }

                                addPoints(farthestPlr, GameManager.pointsPerTask);
                                loadNextTask(ChatColor.YELLOW + farthestPlr.getName() + " has won the task!");
                                isSpecial = true;
                            }

                            else if (ot.getInstruction().equals("Most fish caught in time given")) {

                                if (!fishesCounter.isEmpty()) {
                                    Player winner = fishesCounter.entrySet().stream().max((entry1, entry2) ->
                                            entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
                                    addPoints(winner, GameManager.pointsPerTask);
                                    loadNextTask(ChatColor.YELLOW + winner.getName() + " has won the task!");
                                }
                                else {
                                    loadNextTask(ChatColor.YELLOW + "Nobody won the task.");
                                }

                                isSpecial = true;

                            }

                        }
                    }

                    if (!isSpecial) {
                        loadNextTask(ChatColor.YELLOW + "Time has ended. Next task!");
                    }
                    taskStartTime[0] = System.currentTimeMillis();
                }

                lastTask[0] = currentTask;
                float progress = Math.max(1 - elapsed / currentTask.getTaskDuration(), 0.0f);
                getPlugin().bar.setProgress(progress);
            }
        }.runTaskTimer(TaskRace.getPlugin(), 0L, 1);
    }

    public void stopGame() {
        if (!isGameStarted) return;
        isGameStarted = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(p + ChatColor.RED + "The game has been stopped!");
                TaskRace.getPlugin().bar.setTitle(ChatColor.LIGHT_PURPLE + "-| TaskRace |-");
                TaskRace.getPlugin().bar.setProgress(1.0f);
                pointsMap = new HashMap<>();

                if (Bukkit.getScoreboardManager().getMainScoreboard().getObjective("scores") != null)
                    Bukkit.getScoreboardManager().getMainScoreboard().getObjective("scores").unregister();

                for (Player plr : Bukkit.getOnlinePlayers()) {
                    plr.playSound(plr, Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 1f);
                }
            }
        }.runTaskLater(getPlugin(), 4L);

    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void loadNextTask() {
        loadNextTask(null);
    }

    public void loadNextTask(String msg) {
        if (!isGameStarted) return;

        for (Player plr : pointsMap.keySet()) {
            int points = pointsMap.get(plr);

            if (points >= scoreToWin) {
                Bukkit.broadcastMessage(p + ChatColor.AQUA + plr.getName() + " has won the game!");
                stopGame();
            }
        }

        Task lastTask = currentTask;
        do { currentTask = taskManager.getNextTask(); }
        while (currentTask == lastTask);

        if (msg != null) Bukkit.broadcastMessage(p + msg);
        String points = TaskManager.getValueString(getCurrentTask());

        switch (currentTask.getTaskType()) {
            case CHAT:
                ChatTask ct = (ChatTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(
                        "Type \"" + highlight + ct.getMessage() + white + "\" in the chat" + points);
                break;
            case ITEM:
                ItemTask it = (ItemTask) currentTask;
                String friendly = GameUtils.getFriendlyName(it.getItem().name());
                TaskRace.getPlugin().bar.setTitle("Find " + highlight + friendly + points);
                break;
            case ADVANCEMENT:
                AdvancementTask at = (AdvancementTask) currentTask;
                NamespacedKey key = new NamespacedKey(TaskRace.getPlugin(), at.getAdvancement());
                String title = Bukkit.getAdvancement(key).getKey().getKey();
                TaskRace.getPlugin().bar.setTitle("Achieve the advancement \"" + highlight + title + white + "\"" + points);
                break;
            case BIOME:
                BiomeTask bt = (BiomeTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(
                        "Find a " + highlight + GameUtils.getFriendlyName(bt.getBiome().name()) + points);
                break;
            case DEATH:
                DeathTask dt = (DeathTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(
                        "Die with the following message \"" + highlight + dt.getRequiredMessage() + white + "\"" + points);
                break;
            case EFFECT:
                EffectTask et = (EffectTask) currentTask;
                String effectName = GameUtils.getFriendlyName(et.getEffectType().getName());
                TaskRace.getPlugin().bar.setTitle("Gain the effect " + highlight + effectName + points);
                break;
            case PUNCH:
                PunchTask pt = (PunchTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(
                        "Punch a " + highlight + GameUtils.getFriendlyName(pt.getEntityType().name()) + points);
                break;
            case KILL:
                KillTask kt = (KillTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(
                        "Kill a " + highlight + GameUtils.getFriendlyName(kt.getEntityType().name()) + points);
                break;
            case OTHER:
                OtherTask ot = (OtherTask) currentTask;
                TaskRace.getPlugin().bar.setTitle(ot.getInstruction() + points);
                break;
        }

        if (isGameStarted) {
            for (Player plr : Bukkit.getOnlinePlayers()) {
                plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1f, 1f);
            }
        }

        notAtSpawn = new ArrayList<>();
        bellsMap = new HashMap<>();
        startLocs = new HashMap<>();
        fishesCounter = new HashMap<>();

        if (getCurrentTask().getTaskType() == TaskTypes.ITEM) {
            List<Player> won = new ArrayList<>();
            for (Player plr : Bukkit.getOnlinePlayers()) {
                ItemTask task = (ItemTask) getCurrentTask();
                if (plr.getInventory().contains(task.getItem())) {
                    addPoints(plr, pointsPerTask);
                    won.add(plr);
                }
            }

            if (won.size() == 1) {
                loadNextTask(ChatColor.YELLOW + won.get(0).getName() + " has won the task!");
            }

            else if (won.size() > 1) {
                loadNextTask(ChatColor.YELLOW + "Multiple players have won the task!");
            }
        }

        if (getCurrentTask().getTaskType() == TaskTypes.OTHER) {
            OtherTask ot = (OtherTask) getCurrentTask();
            if (ot.getInstruction().equals("Manhunt!")) {
                if (Bukkit.getOnlinePlayers().size() > 1) {
                    int index = new Random().nextInt(Bukkit.getOnlinePlayers().size());
                    hunted = (Player) Bukkit.getOnlinePlayers().toArray()[index];
                    hunted.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    String title = ChatColor.LIGHT_PURPLE + "Hunt " + hunted.getName() + ", they are at spawn" + points;
                    TaskRace.getPlugin().bar.setTitle(title);

                    ItemStack tracker = new ItemStack(Material.COMPASS);
                    for (Player plr : Bukkit.getOnlinePlayers()) {
                        if (plr.equals(hunted)) continue;
                        plr.getInventory().addItem(tracker);
                    }
                }
                else {
                    loadNextTask();
                }
            }

            else if (ot.getInstruction().equals("Go to coordinates")) {
                Random rand = new Random();
                int x = rand.nextInt(500) * (Math.random() > 0.5 ? 1 : -1);
                int y = rand.nextInt(100);
                int z = rand.nextInt(500) * (Math.random() > 0.5 ? 1 : -1);

                coords = new Vector(x, y, z);
                String title = "Go to " + ChatColor.LIGHT_PURPLE + " " + x + ", " + y + ", " + z + points;
                getPlugin().bar.setTitle(title);
            }

            else if (ot.getInstruction().equals("Return to spawn")) {
                notAtSpawn.addAll(Bukkit.getOnlinePlayers());
            }

            else if (ot.getInstruction().equals("Travel furthest in time given")) {
                for (Player plr : Bukkit.getOnlinePlayers()) {
                    startLocs.put(plr, plr.getLocation());
                }
            }

        }

        updateScoreboard();
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public HashMap<Player, Integer> getPointsMap() {
        return pointsMap;
    }

    public void addPoints(Player plr, int points) {
        if (pointsMap.containsKey(plr)) {
            int toGive = TaskManager.getTaskValue(currentTask, points);

            if (toGive == 0) {
                toGive = points;
            }

            pointsMap.put(plr, toGive + pointsMap.get(plr));
        }
    }

    public void updateScoreboard() {
        List<Map.Entry<Player, Integer>> sorted = pointsMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toList());


        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard sc = manager.getMainScoreboard();

        if (sc.getObjective("scores") != null)
            sc.getObjective("scores").unregister();

        Objective objective = sc.registerNewObjective("scores", "none", highlight + "Scores");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < sorted.size(); i++) {

            String plrName = sorted.get(i).getKey().getName();
            int points = sorted.get(i).getValue();
            objective.getScore(plrName + " - " + ChatColor.BLUE + points + " points").setScore(i);
        }
    }
}
