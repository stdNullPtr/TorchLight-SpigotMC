package com.stdnullptr;

import com.stdnullptr.constants.Commands;
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

    private static final String ON_STR = "on";
    private static final String OFF_STR = "off";
    private static final String TIME_STR = "time";
    private final List<Listener> listeners = new ArrayList<>();

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

        if (args.length == 0) {
            sender.sendMessage(Commands.TORCHLIGHT_USAGE_HINT);
            return false;
        }

        final String firstArg = args[0].toLowerCase();
        switch (firstArg) {
            case ON_STR:
                togglePluginState(true);
                sender.sendMessage("TorchLight has been enabled.");
                break;
            case OFF_STR:
                togglePluginState(false);
                sender.sendMessage("TorchLight has been disabled.");
                break;
            case TIME_STR:
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
                    getConfig().set(Config.TORCHLIGHT_LIGHT_TIMER, seconds);
                    saveConfig();
                    listeners.forEach(listener -> {
                        if (listener instanceof final PlayerMoveEventListener playerMoveEventListener) {
                            playerMoveEventListener.updateCleanupDelay();
                        }
                    });
                    sender.sendMessage("Light time set to " + seconds + " seconds.");
                } catch (final NumberFormatException e) {
                    sender.sendMessage("Invalid number format. " + Commands.TORCHLIGHT_TIMER_USAGE_HINT);
                    return false;
                }
                break;
            default:
                sender.sendMessage(Commands.TORCHLIGHT_USAGE_HINT);
                return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String alias,
            @NotNull final String[] args
    ) {
        if (command.getName().equalsIgnoreCase(TORCHLIGHT)) {

            final List<String> suggestions = new ArrayList<>();

            if (args.length == 1) {
                final String firstArg = args[0].toLowerCase();
                if (ON_STR.startsWith(firstArg)) {
                    suggestions.add(ON_STR);
                }
                if (OFF_STR.startsWith(firstArg)) {
                    suggestions.add(OFF_STR);
                }
                if (TIME_STR.startsWith(firstArg)) {
                    suggestions.add(TIME_STR);
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase(TIME_STR)) {
                // Add some common time suggestions
                suggestions.add("1");
                suggestions.add("3");
                suggestions.add("5");
                suggestions.add("10");
                suggestions.add("30");
            }

            return suggestions;
        }

        return null;
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
