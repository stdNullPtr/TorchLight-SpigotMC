package com.stdnullptr.command.impl;

import com.stdnullptr.PluginMain;
import com.stdnullptr.command.TorchLightCommand;
import com.stdnullptr.constants.Commands;
import com.stdnullptr.constants.Config;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to set the light timer duration
 */
public class TimeCommand implements TorchLightCommand {
    private static final List<String> SUGGESTIONS = Arrays.asList("01", "03", "05", "10", "30");
    private final PluginMain plugin;

    public TimeCommand(final PluginMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Commands.TORCHLIGHT_TIMER_USAGE_HINT);
            return false;
        }

        try {
            final int seconds = Integer.parseInt(args[1]);
            if (seconds < 1 || seconds > 30) {
                sender.sendMessage("Time must be between 1 and 30 seconds.");
                return false;
            }

            plugin.getConfig().set(Config.TORCHLIGHT_LIGHT_TIMER, seconds);
            plugin.saveConfig();
            plugin.updatePlayerMoveListenerTimers();

            sender.sendMessage("Light time set to " + seconds + " seconds.");
            return true;
        } catch (final NumberFormatException e) {
            sender.sendMessage("Invalid number format. " + Commands.TORCHLIGHT_TIMER_USAGE_HINT);
            return false;
        }
    }

    @Override
    public List<String> tabComplete(final String[] args) {
        if (args.length == 2) {
            final String input = args[1].toLowerCase();
            final List<String> result = new ArrayList<>();

            for (final String suggestion : SUGGESTIONS) {
                if (suggestion.startsWith(input)) {
                    result.add(suggestion);
                }
            }

            return result;
        }

        return new ArrayList<>();
    }
}
