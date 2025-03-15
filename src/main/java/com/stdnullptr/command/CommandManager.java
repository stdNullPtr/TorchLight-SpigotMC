package com.stdnullptr.command;

import com.stdnullptr.PluginMain;
import com.stdnullptr.command.impl.*;
import com.stdnullptr.constants.Commands;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages command registration and execution for the TorchLight plugin
 */
public class CommandManager {
    private final Map<String, TorchLightCommand> commands = new HashMap<>();

    /**
     * Initializes the command manager with all available commands
     *
     * @param plugin The plugin instance
     */
    public CommandManager(final PluginMain plugin) {
        commands.put("on", new OnCommand(plugin));
        commands.put("off", new OffCommand(plugin));
        commands.put("time", new TimeCommand(plugin));
        commands.put("status", new StatusCommand(plugin));
        commands.put("light-level", new LightLevelCommand(plugin));
    }

    /**
     * Executes a command based on the provided arguments
     *
     * @param sender The command sender
     * @param args   The command arguments
     * @return true if the command was executed successfully, false otherwise
     */
    public boolean executeCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Commands.TORCHLIGHT_USAGE_HINT);
            return false;
        }

        final TorchLightCommand command = commands.get(args[0].toLowerCase());
        if (command == null) {
            sender.sendMessage(Commands.TORCHLIGHT_USAGE_HINT);
            return false;
        }

        return command.execute(sender, args);
    }

    /**
     * Provides tab completion options for a command
     *
     * @param args The current command arguments
     * @return A list of possible completions
     */
    public List<String> tabCompleteCommand(final String[] args) {
        final List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            final String arg = args[0].toLowerCase();
            for (final String cmd : commands.keySet()) {
                if (cmd.startsWith(arg)) {
                    suggestions.add(cmd);
                }
            }
            return suggestions;
        }

        final TorchLightCommand command = commands.get(args[0].toLowerCase());
        if (command != null) {
            return command.tabComplete(args);
        }

        return suggestions;
    }
}
