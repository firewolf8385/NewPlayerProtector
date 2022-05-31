package com.github.firewolf8385.newplayerprotector;

import com.github.firewolf8385.newplayerprotector.listeners.EntityDamageByEntityListener;
import com.github.firewolf8385.newplayerprotector.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * This plugin protects players who join for the first time from being attacked
 * by other players for a set time, configurable in config.yml.
 */
public final class NewPlayerProtector extends JavaPlugin {
    private final Collection<UUID> protectedPlayers = new ArrayList<>();

    /**
     * This is called when Bukkit first loads the plugin.
     */
    @Override
    public void onEnable() {
        // Creates the configuration file.
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        new File(this.getDataFolder(), "config.yml");
        this.saveConfig();

        // Registers listeners
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    /**
     * Gets the list of the uuids of all currently protected players.
     * UUIDs are used in case the player logs out and back in.
     * @return All protected players' uuids.
     */
    public Collection<UUID> getProtectedPlayers() {
        return protectedPlayers;
    }
}
