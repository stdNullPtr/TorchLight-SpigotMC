package com.stdnullptr;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public class OffhandTorchLight extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getLogger().info("Offhand Torch Light is successfully enabled!");
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		getLogger().info("Offhand Torch Light is successfully disabled!");
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

		// Schedule task to revert the block to its original state
		new BukkitRunnable() {
			@Override
			public void run() {
				if (block.getType() == Material.LIGHT) {
					block.setType(Material.AIR);
				}
			}
		}.runTaskLater(this, 20); // Change back after 1 second (20 ticks)
	}
}