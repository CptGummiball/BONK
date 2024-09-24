package org.cptgummiball.bonk;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EventListeners implements Listener {

    private final BONK plugin;
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    public EventListeners(BONK plugin, ItemManager itemManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemManager.isBonkItem(itemInHand)) {
                Entity target = event.getEntity();
                configManager.handleBonkEffects(player, target);
            }
        }
    }
}

