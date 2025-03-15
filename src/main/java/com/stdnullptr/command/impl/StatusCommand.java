package com.stdnullptr.command.impl;

import com.stdnullptr.PluginMain;
import com.stdnullptr.command.TorchLightCommand;
import com.stdnullptr.constants.Config;
import com.stdnullptr.constants.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to display current TorchLight settings
 */
public class StatusCommand implements TorchLightCommand {
    private final PluginMain plugin;

    public StatusCommand(final PluginMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] args) {
        final boolean enabled = plugin.getConfig().getBoolean(Config.TORCHLIGHT_ENABLED);
        final int timer = plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER);
        final int lightLevel = plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_LEVEL);

        sender.sendMessage("%sTorchLight Status:".formatted(ChatColor.GOLD));
        sender.sendMessage("%s- Enabled: %s".formatted(
                ChatColor.GRAY,
                enabled ? "%sYes".formatted(ChatColor.GREEN) : "%sNo".formatted(ChatColor.RED)
        ));
        sender.sendMessage("%s- Light Duration: %s%d seconds (%d-%d)".formatted(
                ChatColor.GRAY,
                ChatColor.AQUA,
                timer,
                Config.MINIMUM_LIGHT_TIME_SECONDS,
                Config.MAXIMUM_LIGHT_TIME_SECONDS
        ));
        sender.sendMessage("%s- Light Level: %s%d (%d-%d)".formatted(
                ChatColor.GRAY,
                ChatColor.AQUA,
                lightLevel,
                Game.MINIMUM_LIGHT_LEVEL,
                Game.MAXIMUM_LIGHT_LEVEL
        ));
        return true;
    }

    @Override
    public List<String> tabComplete(final String[] args) {
        // No tab completions for this command
        return new ArrayList<>();
    }
}
