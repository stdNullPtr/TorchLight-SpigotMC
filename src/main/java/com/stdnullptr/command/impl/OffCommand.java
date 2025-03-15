package com.stdnullptr.command.impl;

import com.stdnullptr.PluginMain;
import com.stdnullptr.command.TorchLightCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to disable the TorchLight plugin
 */
public class OffCommand implements TorchLightCommand {
    private final PluginMain plugin;

    public OffCommand(final PluginMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, final String[] args) {
        plugin.disableTorchLight();
        sender.sendMessage("TorchLight has been disabled.");
        return true;
    }

    @Override
    public List<String> tabComplete(final String[] args) {
        // No tab completions for this command
        return new ArrayList<>();
    }
}
