package com.github.glatinis.taskrace.commands;

import com.github.glatinis.taskrace.TaskRace;
import com.github.glatinis.taskrace.managers.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.github.glatinis.taskrace.TaskRace.*;

public class TaskRaceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(p + ChatColor.RED + "You must be operator to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(p + ChatColor.RED + "Invalid usage.");
        }

        GameManager gm = TaskRace.getGameManager();

        if (args[0].equalsIgnoreCase("start")) {
            if (!gm.isGameStarted()) {
                gm.startGame();
            } else {
                sender.sendMessage(p + ChatColor.RED + "Game has already started.");
            }
        }

        else if (args[0].equalsIgnoreCase("stop")) {
            if (gm.isGameStarted()) {
                gm.stopGame();
            } else {
                sender.sendMessage(p + ChatColor.RED + "Game has already been stopped.");
            }
        }

        else if (args[0].equalsIgnoreCase("skip")) {
            if (gm.isGameStarted()) {
                gm.loadNextTask();
                sender.sendMessage(p + ChatColor.GREEN + "Skipped the current task.");
            } else {
                sender.sendMessage(p + ChatColor.RED + "Cannot skip when a game has not started.");
            }
        }

        else if (args[0].equalsIgnoreCase("win")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Correct usage: /taskrace win <score to win>");
                return true;
            }

            try {
                Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "You must enter an integer.");
                return true;
            }

            int winScore =  Integer.parseInt(args[1]);
            getPlugin().getConfig().set("winscore", winScore);
            getGameManager().scoreToWin = winScore;

            sender.sendMessage(ChatColor.GREEN + "Set win score to " + winScore + ".");
        }

        return true;
    }
}
