package com.stdnullptr.command.impl;

import com.stdnullptr.PluginMain;
import com.stdnullptr.command.TorchLightCommand;
import com.stdnullptr.constants.Config;
import com.stdnullptr.constants.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to set the torch light brightness level
 */
public class LightLevelCommand implements TorchLightCommand {
    private static final List<String> SUGGESTIONS = Arrays.asList("1", "10", "15");
    private final PluginMain plugin;

    public LightLevelCommand(final PluginMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage("%sUsage: /torchlight light-level <level>".formatted(ChatColor.RED));
            sender.sendMessage("%sLevel must be between %d and %d".formatted(ChatColor.GRAY, Game.MINIMUM_LIGHT_LEVEL, Game.MAXIMUM_LIGHT_LEVEL));
            return false;
        }

        try {
            final int level = Integer.parseInt(args[1]);
            if (level < Game.MINIMUM_LIGHT_LEVEL || level > Game.MAXIMUM_LIGHT_LEVEL) {
                sender.sendMessage("%sLight level must be between %d and %d.".formatted(
                        ChatColor.RED,
                        Game.MINIMUM_LIGHT_LEVEL,
                        Game.MAXIMUM_LIGHT_LEVEL
                ));
                return false;
            }

            plugin.getConfig().set(Config.TORCHLIGHT_LIGHT_LEVEL, level);
            plugin.saveConfig();

            sender.sendMessage("%sLight level set to %d.".formatted(ChatColor.GREEN, level));
            return true;
        } catch (final NumberFormatException e) {
            sender.sendMessage("%sInvalid number format. Light level must be an integer.".formatted(ChatColor.RED));
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
