package com.stdnullptr;

import com.stdnullptr.command.CommandManager;
import com.stdnullptr.constants.Config;
import com.stdnullptr.constants.Game;
import com.stdnullptr.constants.Permissions;
import com.stdnullptr.listener.PlayerActivityEventListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.stdnullptr.constants.Commands.TORCHLIGHT;

public class PluginMain extends JavaPlugin {
    private final List<Listener> listeners = new ArrayList<>();
    private CommandManager commandManager;

    /***
     * Change only with {@link #togglePluginState}
     */
    private volatile boolean isEnabled;

    /**
     * Triggered on server startup or plugin reload, NOT ON 'on' command
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();

        validateExistingConfig();

        isEnabled = getConfig().getBoolean(Config.TORCHLIGHT_ENABLED, true);

        listeners.add(new PlayerActivityEventListener(this));

        if (isEnabled) {
            registerListeners();
        }

        commandManager = new CommandManager(this);

        getLogger().info("TorchLight is successfully enabled!");
    }

    /**
     * Triggered on server shutdown or plugin unload, NOT ON 'off' command
     */
    @Override
    public void onDisable() {
        unregisterListeners();
        getLogger().info("TorchLight is disabled, all listeners have been unregistered and all lights removed.");
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, final Command command, @NotNull final String label, final String[] args) {
        if (!command.getName().equalsIgnoreCase(TORCHLIGHT)) {
            return false;
        }

        if (!sender.hasPermission(Permissions.TORCHLIGHT_TOGGLE)) {
            sender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        return commandManager.executeCommand(sender, args);
    }

    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String alias,
            @NotNull final String[] args
    ) {
        if (command.getName().equalsIgnoreCase(TORCHLIGHT)) {
            return commandManager.tabCompleteCommand(args);
        }

        return null;
    }

    public void enableTorchLight() {
        togglePluginState(true);
    }

    public void disableTorchLight() {
        togglePluginState(false);
    }

    private void registerListeners() {
        getLogger().info("Registering listeners...");

        final PluginManager pm = getServer().getPluginManager();
        listeners.forEach(listener -> pm.registerEvents(listener, this));
    }

    private void unregisterListeners() {
        getLogger().info("Unregistering listeners...");
        listeners.forEach(HandlerList::unregisterAll);
    }

    private synchronized void togglePluginState(final boolean newState) {
        if (newState == isEnabled) {
            return;
        }

        isEnabled = newState;

        if (isEnabled) {
            registerListeners();
        } else {
            listeners.forEach(listener -> {
                if (listener instanceof final PlayerActivityEventListener playerActivityEventListener) {
                    playerActivityEventListener.cleanupAllLights();
                }
            });
            unregisterListeners();
        }

        getConfig().set(Config.TORCHLIGHT_ENABLED, isEnabled);
        saveConfig();
    }

    private void validateExistingConfig() {
        if (!getConfig().contains(Config.TORCHLIGHT_ENABLED) || !getConfig().isBoolean(Config.TORCHLIGHT_ENABLED)) {
            getConfig().set(Config.TORCHLIGHT_ENABLED, Config.DEFAULT_PLUGIN_STATE);
            saveConfig();
        }

        if (!getConfig().contains(Config.TORCHLIGHT_LIGHT_TIMER)
                || !getConfig().isInt(Config.TORCHLIGHT_LIGHT_TIMER)
                || getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER) < Config.MINIMUM_LIGHT_TIME_SECONDS
                || getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER) > Config.MAXIMUM_LIGHT_TIME_SECONDS
        ) {
            getConfig().set(Config.TORCHLIGHT_LIGHT_TIMER, Config.DEFAULT_LIGHT_TIME_SECONDS);
            saveConfig();
        }

        if (!getConfig().contains(Config.TORCHLIGHT_LIGHT_LEVEL)
                || !getConfig().isInt(Config.TORCHLIGHT_LIGHT_LEVEL)
                || getConfig().getInt(Config.TORCHLIGHT_LIGHT_LEVEL) < Game.MINIMUM_LIGHT_LEVEL
                || getConfig().getInt(Config.TORCHLIGHT_LIGHT_LEVEL) > Game.MAXIMUM_LIGHT_LEVEL
        ) {
            getConfig().set(Config.TORCHLIGHT_LIGHT_LEVEL, Config.DEFAULT_LIGHT_LEVEL);
            saveConfig();
        }
    }
}
