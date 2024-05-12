package com.stdnullptr;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener {

	public static final String ON_STR = "on";
	public static final String OFF_STR = "off";
	public static final int CLEANUP_DELAY = 3 * 20; // 1 second = 20 ticks
	public static final String TORCHLIGHT_ENABLED_STR = "torchlight-enabled";
	public static final String TORCHLIGHT_CMD_STR = "torchlight";

	private final Set<Location> litBlocks = new HashSet<>();
	private boolean enabled = true;

	@Override
	public void onEnable() {
		if (!getConfig().contains(TORCHLIGHT_ENABLED_STR)) {
			getConfig().set(TORCHLIGHT_ENABLED_STR, true);
			saveConfig();
		}

		enabled = getConfig().getBoolean(TORCHLIGHT_ENABLED_STR, true);
		Bukkit.getPluginManager().registerEvents(this, this);
		getLogger().info("Offhand Torch Light is successfully enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Offhand Torch Light is being disabled, cleaning up light blocks...");

		// Ensure all lit blocks are reverted to air
		for (Location location : litBlocks) {
			Block block = location.getBlock();
			if (block.getType() == Material.LIGHT) {
				block.setType(Material.AIR);
			}
		}

		litBlocks.clear();

		getLogger().info("All light blocks have been cleaned up.");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!enabled) {
			return;
		}

		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.SPECTATOR) {
			return;
		}

		ItemStack offHandItem = player.getInventory().getItemInOffHand();

		if (offHandItem.getType() != Material.TORCH) {
			return;
		}

		Location playerLocation = player.getLocation();
		Location blockLocationAtPlayer = playerLocation.getBlock().getLocation();
		Block block = blockLocationAtPlayer.getBlock();

		if (block.getType() != Material.AIR) {
			return;
		}

		block.setType(Material.LIGHT);
		litBlocks.add(blockLocationAtPlayer);

		// Schedule task to revert the block to its original state
		new BukkitRunnable() {
			@Override
			public void run() {
				if (block.getType() == Material.LIGHT) {
					block.setType(Material.AIR);
					litBlocks.remove(blockLocationAtPlayer);
				}
			}
		}.runTaskLater(this, CLEANUP_DELAY);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
		if (!command.getName().equalsIgnoreCase(TORCHLIGHT_CMD_STR) || args.length != 1) {
			return false;
		}

		if (!sender.hasPermission("torchlight.toggle")) {
			sender.sendMessage("You do not have permission to use this command.");
			return false;
		}

		String firstArg = args[0].toLowerCase();
		switch (firstArg) {
			case ON_STR:
				enabled = true;
				getConfig().set(TORCHLIGHT_ENABLED_STR, true);
				saveConfig();
				sender.sendMessage("Offhand torch lighting has been enabled.");
				break;
			case OFF_STR:
				enabled = false;
				getConfig().set(TORCHLIGHT_ENABLED_STR, false);
				saveConfig();
				sender.sendMessage("Offhand torch lighting has been disabled.");
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
}