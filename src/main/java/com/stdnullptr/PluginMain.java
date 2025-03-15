package com.stdnullptr;

import com.stdnullptr.command.CommandManager;
import com.stdnullptr.constants.Config;
import com.stdnullptr.constants.Permissions;
import com.stdnullptr.listener.PlayerMoveEventListener;
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

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().contains(Config.TORCHLIGHT_ENABLED)) {
            getConfig().set(Config.TORCHLIGHT_ENABLED, true);
            saveConfig();
        }

        if (!getConfig().contains(Config.TORCHLIGHT_LIGHT_TIMER)) {
            getConfig().set(Config.TORCHLIGHT_LIGHT_TIMER, 3);
            saveConfig();
        }

        isEnabled = getConfig().getBoolean(Config.TORCHLIGHT_ENABLED, true);

        final PlayerMoveEventListener playerMoveEventListener = new PlayerMoveEventListener(this);
        listeners.add(playerMoveEventListener);
        if (isEnabled) {
            registerListeners();
        }

        commandManager = new CommandManager(this);

        getLogger().info("TorchLight is successfully enabled!");
    }

    @Override
    public void onDisable() {
        unregisterListeners();
        getLogger().info("TorchLight is disabled and all listeners have been unregistered.");
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

    public void updatePlayerMoveListenerTimers() {
        listeners.forEach(listener -> {
            if (listener instanceof final PlayerMoveEventListener playerMoveEventListener) {
                playerMoveEventListener.updateCleanupDelay();
            }
        });
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
            unregisterListeners();
        }

        getConfig().set(Config.TORCHLIGHT_ENABLED, isEnabled);
        saveConfig();
    }
}
