package com.stdnullptr.listener;

import com.stdnullptr.PluginMain;
import com.stdnullptr.constants.Config;
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
    private static final int TICKS_ONE_SECOND = 20; // 1 second = 20 ticks

    private final PluginMain plugin;
    private int cleanupDelay;

    public PlayerMoveEventListener(final PluginMain plugin) {
        this.plugin = plugin;
        cleanupDelay = TICKS_ONE_SECOND * plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {

        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        final ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (offHandItem.getType() != Material.TORCH) {
            return;
        }

        final Location playerLocation = player.getLocation();
        final Location blockLocationAtPlayer = playerLocation.getBlock().getLocation();

        final Block block = blockLocationAtPlayer.getBlock();
        if (block.getType() != Material.AIR) {
            return;
        }

        block.setType(Material.LIGHT);

        // Schedule task to revert the block to its original state
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (block.getType() == Material.LIGHT) {
                block.setType(Material.AIR);
            }
        }, cleanupDelay);
    }

    public void updateCleanupDelay() {
        cleanupDelay = TICKS_ONE_SECOND * plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER);
    }
}
