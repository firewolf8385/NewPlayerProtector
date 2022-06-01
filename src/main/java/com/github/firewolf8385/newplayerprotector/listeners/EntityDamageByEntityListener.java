package com.github.firewolf8385.newplayerprotector.listeners;

import com.github.firewolf8385.newplayerprotector.NewPlayerProtector;
import com.github.firewolf8385.newplayerprotector.utilities.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This listens to the EntityDamageByEntity event, which is called every time one entity damages another.
 * We use this to prevent players from damaging protected players.
 */
public class EntityDamageByEntityListener implements Listener {
    private final NewPlayerProtector plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * This is known as Dependency Injection.
     * @param plugin Instance of the plugin.
     */
    public EntityDamageByEntityListener(NewPlayerProtector plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event EntityDamageByEntityEvent.
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // Makes sure the entity is a player.
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Makes sure the player is protected.
        if(!plugin.getProtectedPlayers().contains(player.getUniqueId())) {
            return;
        }

        // Checks if the damager is a projectile.
        if(event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getEntity();

            // Makes sure the projectile shooter is a player.
            if(!(projectile.getShooter() instanceof Player)) {
                return;
            }

            // Cancels the event and sends a warning message to the shooter.
            Player shooter = (Player) projectile.getShooter();
            event.setCancelled(true);
            ChatUtils.chat(shooter, plugin.getConfig().getString("CannotAttackMessage"));
            return;
        }

        // Checks if the damager is another player.
        if(event.getDamager() instanceof Player) {
            // Cancels the event and sends a warning message to the attacker.
            Player attacker = (Player) event.getDamager();
            event.setCancelled(true);
            ChatUtils.chat(attacker, plugin.getConfig().getString("CannotAttackMessage"));
        }
    }
}
