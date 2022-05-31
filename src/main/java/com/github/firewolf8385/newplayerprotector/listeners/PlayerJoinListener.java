package com.github.firewolf8385.newplayerprotector.listeners;

import com.github.firewolf8385.newplayerprotector.NewPlayerProtector;
import com.github.firewolf8385.newplayerprotector.utilities.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This listens to the PlayerJoinEvent event, which is called every time a player joins the server.
 * We use this to check if the player is joining for the first time, and if so applying the projections.
 */
public class PlayerJoinListener implements Listener {
    private final NewPlayerProtector plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * This is known as Dependency Injection.
     * @param plugin Instance of the plugin.
     */
    public PlayerJoinListener(NewPlayerProtector plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Exit if the player has played before.
        if(!player.hasPlayedBefore()) {
            return;
        }

        // Add the player to the protected players list.
        plugin.getProtectedPlayers().add(player.getUniqueId());

        // After the amount of time set in config.yml, remove them from the list.
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Removes the player from the protected players list.
            plugin.getProtectedPlayers().remove(player.getUniqueId());

            // Tells the player they can now be attacked.
            ChatUtils.chat(player, plugin.getConfig().getString("ExpiredMessage"));
        }, plugin.getConfig().getInt("ProtectedTime") * 20L);
    }
}
