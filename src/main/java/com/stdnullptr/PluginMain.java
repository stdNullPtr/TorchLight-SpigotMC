package com.stdnullptr;

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

public class PluginMain extends JavaPlugin {

	private static final String ON_STR = "on";
	private static final String OFF_STR = "off";
	private static final String TORCHLIGHT_ENABLED_STR = "torchlight-enabled";
	private static final String TORCHLIGHT_PERMISSION_TOGGLE_STR = "torchlight.toggle";
	private static final String TORCHLIGHT_CMD_STR = "torchlight";

	private final List<Listener> listeners = new ArrayList<>();

	/***
	 * Change only with {@link #togglePluginState}
	 */
	private volatile boolean isEnabled;

	@Override
	public void onEnable() {
		if (!getConfig().contains(TORCHLIGHT_ENABLED_STR)) {
			getConfig().set(TORCHLIGHT_ENABLED_STR, true);
			saveConfig();
		}

		isEnabled = getConfig().getBoolean(TORCHLIGHT_ENABLED_STR, true);

		PlayerMoveEventListener playerMoveEventListener = new PlayerMoveEventListener(this);
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
	public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
		if (!command.getName().equalsIgnoreCase(TORCHLIGHT_CMD_STR) || args.length != 1) {
			return false;
		}

		if (!sender.hasPermission(TORCHLIGHT_PERMISSION_TOGGLE_STR)) {
			sender.sendMessage("You do not have permission to use this command.");
			return false;
		}

		String firstArg = args[0].toLowerCase();
		switch (firstArg) {
			case ON_STR:
				togglePluginState(true);
				sender.sendMessage("TorchLight has been enabled.");
				break;
			case OFF_STR:
				togglePluginState(false);
				sender.sendMessage("TorchLight has been disabled.");
				break;
			default:
				sender.sendMessage("Usage: /torchlight <on | off>");
				return false;
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (command.getName().equalsIgnoreCase(TORCHLIGHT_CMD_STR)) {

			List<String> suggestions = new ArrayList<>();

			if (args.length == 1) {
				String firstArg = args[0].toLowerCase();
				if (firstArg.startsWith(ON_STR)) {
					suggestions.add(ON_STR);
				}

				if (firstArg.startsWith(OFF_STR)) {
					suggestions.add(OFF_STR);
				}
			}

			return suggestions;
		}

		return null;
	}

	private void registerListeners() {
		getLogger().info("Registering listeners...");

		PluginManager pm = getServer().getPluginManager();
		listeners.forEach(listener -> pm.registerEvents(listener, this));
	}

	private void unregisterListeners() {
		getLogger().info("Unregistering listeners...");
		listeners.forEach(HandlerList::unregisterAll);
	}

	private void togglePluginState(boolean newState) {
		if (newState == isEnabled) {
			return;
		}

		isEnabled = newState;

		if (isEnabled) {
			registerListeners();
		} else {
			unregisterListeners();
		}

		getConfig().set(TORCHLIGHT_ENABLED_STR, isEnabled);
		saveConfig();
	}
}