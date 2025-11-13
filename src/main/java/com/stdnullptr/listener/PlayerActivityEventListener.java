package com.stdnullptr.listener;

import com.stdnullptr.PluginMain;
import com.stdnullptr.constants.Config;
import com.stdnullptr.constants.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerActivityEventListener implements Listener {
    private static final long MOVEMENT_THROTTLE_MS = 200; // Only update every 200ms

    private final PluginMain plugin;

    // Track player lights, have only 1 light per player for smoother experience (no lagging lights behind player, like a real torch)
    private final Map<UUID, Location> playerLightLocations = new HashMap<>();
    private final Map<UUID, Long> lastLightUpdate = new HashMap<>();

    public PlayerActivityEventListener(final PluginMain plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        
        // Skip if off permission
        if(player.hasPermission("torchlight.off") == true) {
        	return;
        }

        // Skip if moved too recently
        final long currentTime = System.currentTimeMillis();
        if (lastLightUpdate.containsKey(playerId) && currentTime - lastLightUpdate.get(playerId) < MOVEMENT_THROTTLE_MS) {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        // Remove existing light if player no longer has torch
        if (player.getInventory().getItemInOffHand().getType() != Material.TORCH) {
            removeExistingLight(playerId);
            return;
        }

        final Location playerLocation = player.getLocation();
        final Location blockLocationAtPlayer = playerLocation.getBlock().getLocation();

        // Don't replace if we already have a light at this location
        if (playerLightLocations.containsKey(playerId) && playerLightLocations.get(playerId).equals(blockLocationAtPlayer)) {
            return;
        }

        final Block blockAtPlayerLocation = blockLocationAtPlayer.getBlock();
        if (blockAtPlayerLocation.getType() != Material.AIR) {
            return;
        }

        // Remove previous light if it exists
        removeExistingLight(playerId);

        // Place new light with configured light level
        blockAtPlayerLocation.setType(Material.LIGHT);
        final BlockData blockData = blockAtPlayerLocation.getBlockData();
        if (blockData instanceof final Light light) {
            final int lightLevel = plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_LEVEL, Game.MAXIMUM_LIGHT_LEVEL);
            light.setLevel(lightLevel);
            blockAtPlayerLocation.setBlockData(light);
        }

        playerLightLocations.put(playerId, blockLocationAtPlayer);
        lastLightUpdate.put(playerId, currentTime);

        final long cleanupDelay = (long) Game.TICKS_ONE_SECOND * plugin.getConfig().getInt(Config.TORCHLIGHT_LIGHT_TIMER);

        // Schedule task to revert the block to its original state
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
        	// Skip if permanent permission
        	if(player.hasPermission("torchlight.permanent")) {
            	return;
            }
            if (blockAtPlayerLocation.getType() == Material.LIGHT &&
                    playerLightLocations.containsKey(playerId) &&
                    playerLightLocations.get(playerId).equals(blockLocationAtPlayer)
            ) {
                blockAtPlayerLocation.setType(Material.AIR);
                playerLightLocations.remove(playerId);
            }
        }, cleanupDelay);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();

        removeExistingLight(playerId);
    }

    /**
     * Cleans up all light blocks placed by this plugin
     */
    public void cleanupAllLights() {
        // Create a copy of the keys to avoid concurrent modification
        final Set<UUID> playerIds = new HashSet<>(playerLightLocations.keySet());

        for (final UUID playerId : playerIds) {
            removeExistingLight(playerId);
        }
    }

    private void removeExistingLight(final UUID playerId) {
        if (playerLightLocations.containsKey(playerId)) {
            final Location oldLoc = playerLightLocations.get(playerId);
            final Block oldBlock = oldLoc.getBlock();
            if (oldBlock.getType() == Material.LIGHT) {
                oldBlock.setType(Material.AIR);
            }
            playerLightLocations.remove(playerId);
        }
    }
}
