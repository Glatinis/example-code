package com.github.glatinis.taskrace.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TaskRaceCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "stop", "skip", "win");
        }

        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("win")) {
                return Arrays.asList("<points to win>");
            }
        }

        return Collections.emptyList();
    }
}
