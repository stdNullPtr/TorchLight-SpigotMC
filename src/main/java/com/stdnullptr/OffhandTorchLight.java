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

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class OffhandTorchLight extends JavaPlugin implements Listener {

	private final Set<Location> litBlocks = new HashSet<>();
	private boolean enabled = true;

	@Override
	public void onEnable() {
		getLogger().info("Offhand Torch Light is successfully enabled!");
		Bukkit.getPluginManager().registerEvents(this, this);
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
		}.runTaskLater(this, 20); // Change back after 1 second (20 ticks)
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
		if (command.getName().equalsIgnoreCase("torchlight")) {
			enabled = !enabled;
			String status = enabled ? "enabled" : "disabled";
			sender.sendMessage("Offhand torches emitting light has been " + status + ".");
			return true;
		}
		return false;
	}
}