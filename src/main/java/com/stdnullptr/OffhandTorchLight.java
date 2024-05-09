package com.stdnullptr;

import org.bukkit.Bukkit;
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
public class OffhandTorchLight extends JavaPlugin implements Listener {

	public static final String ON = "on";
	public static final String OFF = "off";
	public static final int CLEANUP_DELAY = 2 * 20; // 1 second = 20 ticks
	public static final String TORCHLIGHT_ENABLED = "torchlight-enabled";
	public static final String TORCHLIGHT = "torchlight";

	private final Set<Location> litBlocks = new HashSet<>();
	private boolean enabled = true;

	@Override
	public void onEnable() {
		if (!getConfig().contains(TORCHLIGHT_ENABLED)) {
			getConfig().set(TORCHLIGHT_ENABLED, true);
			saveConfig();
		}
		enabled = getConfig().getBoolean(TORCHLIGHT_ENABLED, true);
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
		if (!command.getName().equalsIgnoreCase(TORCHLIGHT) || args.length != 1) {
			return false;
		}

		if (!sender.hasPermission("torchlight.toggle")) {
			sender.sendMessage("You do not have permission to use this command.");
			return false;
		}

		switch (args[0].toLowerCase()) {
			case ON:
				enabled = true;
				getConfig().set(TORCHLIGHT_ENABLED, true);
				saveConfig();
				sender.sendMessage("Offhand torch lighting has been enabled.");
				break;
			case OFF:
				enabled = false;
				getConfig().set(TORCHLIGHT_ENABLED, false);
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
		if (command.getName().equalsIgnoreCase(TORCHLIGHT)) {
			List<String> suggestions = new ArrayList<>();
			if (args.length == 1) {
				if (ON.startsWith(args[0].toLowerCase())) {
					suggestions.add(ON);
				}
				if (OFF.startsWith(args[0].toLowerCase())) {
					suggestions.add(OFF);
				}
			}
			return suggestions;
		}
		return null;
	}
}