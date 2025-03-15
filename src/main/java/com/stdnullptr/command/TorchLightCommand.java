package com.stdnullptr.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Interface for all TorchLight plugin commands
 */
public interface TorchLightCommand {
    /**
     * Executes the command
     *
     * @param sender The command sender
     * @param args   The command arguments
     * @return true if the command was executed successfully, false otherwise
     */
    boolean execute(CommandSender sender, String[] args);

    /**
     * Provides tab completion options for the command
     *
     * @param args The current command arguments
     * @return A list of possible completions
     */
    List<String> tabComplete(String[] args);
}
