package com.stdnullptr.listener;

import com.stdnullptr.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveEventListener implements Listener {

	private final PluginMain plugin;
	private final int CLEANUP_DELAY;

	public PlayerMoveEventListener(PluginMain plugin) {
		this.plugin = plugin;
		CLEANUP_DELAY = 20 * plugin.getConfig().getInt(plugin.TORCHLIGHT_TIME_STR); // 1 second = 20 ticks
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {

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

		// Schedule task to revert the block to its original state
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (block.getType() == Material.LIGHT) {
				block.setType(Material.AIR);
			}
		}, CLEANUP_DELAY);
	}
}
