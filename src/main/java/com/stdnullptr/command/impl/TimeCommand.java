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
    private static final List<String> SUGGESTIONS = Arrays.asList("1", "3", "5", "10", "30");
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
            final int lightFadeTimeSeconds = Integer.parseInt(args[1]);
            if (lightFadeTimeSeconds < Config.MINIMUM_LIGHT_TIME_SECONDS || lightFadeTimeSeconds > Config.MAXIMUM_LIGHT_TIME_SECONDS) {
                sender.sendMessage("Time must be between %d and %d seconds.".formatted(Config.MINIMUM_LIGHT_TIME_SECONDS, Config.MAXIMUM_LIGHT_TIME_SECONDS));
                return false;
            }

            plugin.getConfig().set(Config.TORCHLIGHT_LIGHT_TIMER, lightFadeTimeSeconds);
            plugin.saveConfig();

            sender.sendMessage("Light time set to %d seconds.".formatted(lightFadeTimeSeconds));
            return true;
        } catch (final NumberFormatException e) {
            sender.sendMessage("Invalid number format. %s".formatted(Commands.TORCHLIGHT_TIMER_USAGE_HINT));
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
